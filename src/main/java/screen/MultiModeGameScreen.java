package screen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import game.block.Block;
import game.multi_play.GameWebSocketClient;
import game.multi_play.MultiModeBoard;
import game.multi_play.OpponentBoard;
import game.multi_play.message.GameActionMessage;
import game.multi_play.message.GameActionType;
import game.multi_play.message.GameStartMessage;
import score.Score;
import score.ScoreDao;
import setting.GameFont;
import setting.GameSettings;
import setting.GameSize;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;

import static setting.Constants.*;
import static setting.GameSettings.GAME_SIZE;

public class MultiModeGameScreen extends GameScreen {
    private final GameWebSocketClient webSocketClient;
    private OpponentBoard opponentBoard;
    private OpponentBoardArea opponentBoardArea;
    private OpponentGameInfoArea opponentGameInfoArea;
    private ObjectMapper objectMapper;

    private int lastScoreForAttackProgress;
    private int opponentScore;
    private int opponentLevel;

    public MultiModeGameScreen(ScoreDao scoreDao) {
        super(scoreDao);
        objectMapper = new ObjectMapper();
        webSocketClient = new GameWebSocketClient("ws://localhost:8080/game");
        webSocketClient.setMessageHandler(this::handleWebSocketMessage);
    }

    private void handleWebSocketMessage(String msg) {
        try {
            GameStartMessage message = objectMapper.readValue(msg, GameStartMessage.class);
            if (message.getAction().equals("START_GAME")) {
                initGame();
                start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void waitOpponent() {
        setLayout(new BorderLayout());

        JLabel waitingLabel = new JLabel("Waiting...", SwingConstants.CENTER);
        waitingLabel.setFont(GameFont.LARGE.getFont()); // 글자 크기와 폰트 설정
        waitingLabel.setForeground(Color.WHITE); // 글자 색상 설정

        add(waitingLabel, BorderLayout.CENTER);
        revalidate();
    }

    public void initGame() {
        initLayout();
        initResource();
        setBoard();
    }

    @Override
    public void start() {
        super.start();
        opponentBoard.setUp();
    }

    private void setBoard() {
        board = new MultiModeBoard(webSocketClient, objectMapper);
        opponentBoard = new OpponentBoard();
    }

    private void initLayout() {
        GameSettings.setGameSize(GameSize.MULTI_MODE);
        GameClient client = (GameClient) getTopLevelAncestor();
        client.resize();
        client.setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel myPanel = new JPanel();
        JPanel opponentPanel = new JPanel();
        boardArea = new BoardArea();
        gameInfoArea = new GameInfoArea();
        opponentBoardArea = new OpponentBoardArea();
        opponentGameInfoArea = new OpponentGameInfoArea();

        myPanel.add(boardArea, BorderLayout.WEST);
        myPanel.add(gameInfoArea, BorderLayout.EAST);
        opponentPanel.add(opponentBoardArea, BorderLayout.WEST);
        opponentPanel.add(opponentGameInfoArea, BorderLayout.EAST);

        add(myPanel, BorderLayout.WEST);
        add(opponentPanel, BorderLayout.EAST);

        revalidate();
    }

    private void initResource() {
        isPaused = false;
        score = 0;
        level = 1;
        opponentScore = 0;
        opponentLevel = 1;

        lastScoreForProgress = 0;
        lastScoreForAttackProgress = 0;

        timer = new Timer(1000, e -> {
            if (!isPaused) triggerMoveBlock();
        });

        setFocusable(true);
        setKeyListener();
        requestFocusInWindow();
    }

    @Override
    protected void triggerMoveBlock() {
        board.moveDown();
        double clearedUnits = board.clearLines();
        addScore(clearedUnits);
        opponentBoard.clearLines();

        if (board.checkGameOver()) {
            lose();
        }

        boardArea.repaint();
        gameInfoArea.repaint();
        opponentBoardArea.repaint();
        opponentGameInfoArea.repaint();
    }

    @Override
    protected void addScore(double unitCnt) {
        if (unitCnt <= 0) return;

        score += (int) (unitCnt * SCORE_UNIT);

        sendMessage(GameActionMessage.setScoreMessage(score));

        while (checkShouldDoLevelProgress()) {
            handleLevelProgression();
        }

        while (checkShouldDoAttackProgress()) {
            handleAttackProgression();
        }
    }

    private boolean checkShouldDoAttackProgress() {
        if (score >= lastScoreForAttackProgress + ATTACK_LINES_THRESHOLD) {
            lastScoreForAttackProgress += ATTACK_LINES_THRESHOLD;
            return true;
        }
        return false;
    }

    private void handleAttackProgression() {
        sendMessage(new GameActionMessage(GameActionType.ATTACK_LINES, null, null));
    }

    private void sendMessage(GameActionMessage gameActionMessage) {
        try {
            String message = objectMapper.writeValueAsString(gameActionMessage);
            webSocketClient.sendMessage(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkShouldDoLevelProgress() {
        if (level < 10) {
            if (score >= lastScoreForProgress + LEVEL_UP_THRESHOLD) {
                lastScoreForProgress += LEVEL_UP_THRESHOLD; // 다음 검사 기준 갱신
                return true;
            }
        }
        return false;
    }

    @Override
    public void handleLevelProgression() {
        if (level < 10) levelUp();
        sendMessage(new GameActionMessage(GameActionType.SET_LEVEL, null, null));
    }

    @Override
    public void levelUp() {
        super.levelUp();
        sendMessage(GameActionMessage.setLevelMessage(level));
    }

    @Override
    public void continueGame() {
        super.continueGame();
        opponentBoardArea.repaint();
        sendMessage(new GameActionMessage(GameActionType.CONTINUE, null, null));
    }

    @Override
    public void pauseGame() {
        super.pauseGame();
        opponentBoardArea.repaint();
        sendMessage(new GameActionMessage(GameActionType.PAUSE, null, null));
    }

    public void pauseGameWithoutMessage() {
        resetKeyListeners();
        super.pauseGame();
        isPaused = true;
        boardArea.repaint();
        opponentBoardArea.repaint();
    }

    public void continueGameWithoutMessage() {
        resetKeyListeners();
        super.continueGame();
        isPaused = false;
        boardArea.repaint();
        opponentBoardArea.repaint();
    }

    public void win() {
        if (timer.isRunning()) {
            timer.stop();
        }

        resetKeyListeners();

        // 현재 점수를 보여주고 닉네임을 입력받음
        String displayMessage = "You WIN!! \nYour Score: " + score + "\nEnter your username:";
        String username = JOptionPane.showInputDialog(this, displayMessage, "Game Over", JOptionPane.PLAIN_MESSAGE);

        if (username != null && !username.trim().isEmpty()) {
            // 입력된 닉네임과 점수를 저장하거나 랭킹에 추가
            Score scoreEntity = new Score(username, score);
            scoreDao.save(scoreEntity);
        }

        GameClient client = (GameClient) getTopLevelAncestor();
        client.goToScoreboardScreen();
    }

    public void lose() {
        if (timer.isRunning()) {
            timer.stop();
        }

        sendMessage(new GameActionMessage(GameActionType.GAME_OVER, null, null));

        resetKeyListeners();

        // 현재 점수를 보여주고 닉네임을 입력받음
        String displayMessage = "You LOSE..\nYour Score: " + score + "\nEnter your username:";
        String username = JOptionPane.showInputDialog(this, displayMessage, "Game Over", JOptionPane.PLAIN_MESSAGE);

        if (username != null && !username.trim().isEmpty()) {
            // 입력된 닉네임과 점수를 저장하거나 랭킹에 추가
            Score scoreEntity = new Score(username, score);
            scoreDao.save(scoreEntity);
        }

        GameClient client = (GameClient) getTopLevelAncestor();
        client.goToScoreboardScreen();
    }

    public class OpponentBoardArea extends JPanel {

        public OpponentBoardArea() {
            setBackground(Color.BLACK);
            setForeground(Color.WHITE);
            setPreferredSize(new Dimension(GAME_SIZE.getGameAreaWidth(), GAME_SIZE.getHeight()));
            webSocketClient.setMessageHandler(this::handleSocketBlockActionMessage);
        }

        private void handleSocketBlockActionMessage(String msg) {
            try {
                GameActionMessage message = objectMapper.readValue(msg, GameActionMessage.class);
                switch (message.getAction()) {
                    case GameActionType.MOVE_BLOCK -> opponentBoard.moveBlock(message.getPosition());
                    case GameActionType.ROTATE_BLOCK -> opponentBoard.rotateBlock();
                    case GameActionType.SPAWN_NEW_BLOCK -> opponentBoard.spawnBlock(message.getBlockType());
                    case GameActionType.REPLACE_NEXT_WITH_CURR -> opponentBoard.replaceNextWithCurr();
                    case GameActionType.FIXED -> opponentBoard.fixBlock();
                    case GameActionType.ATTACK_LINES -> board.addRandomLineWithEmptyCell(); // 상대가 공격을 보냄 -> 내가 피해
                    case GameActionType.ATTACKED ->
                            opponentBoard.addLineWithEmptyCell(message.getEmptyCellIndex()); // 상대가 공격을 받았음을 확인함 -> 상대가 피해받은 상황 랜더링
                    case GameActionType.PAUSE -> pauseGameWithoutMessage();
                    case GameActionType.CONTINUE -> continueGameWithoutMessage();
                    case GameActionType.GAME_OVER -> win();
                    case GameActionType.SET_SCORE -> opponentScore = message.getScore();
                    case GameActionType.SET_LEVEL -> opponentLevel = message.getLevel();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            drawLines(g);
            drawBlock(g);
        }

        private void drawBlock(Graphics g) {
            Block currBlock = opponentBoard.getCurrBlock();
            if (currBlock == null) return;
            int[][] blockShape = currBlock.getShape();
            Color color = currBlock.getBlockType().getColor();

            for (int i = 0; i < currBlock.getHeight(); ++i) {
                for (int j = 0; j < currBlock.getWidth(); ++j) {
                    if (blockShape[i][j] == 1) {
                        int x = currBlock.getPosition().getX() + j * GAME_SIZE.getBlockCellSize();
                        int y = currBlock.getPosition().getY() + i * GAME_SIZE.getBlockCellSize();
                        drawBlockByGraphics(g, color, x, y);
                    }
                }
            }

            // STATUS TEXT
            if (isPaused) {
                g.setColor(Color.WHITE);
                g.setFont(GameFont.SMALL.getFont());
                g.drawString("PAUSE!!, Press 'ESC' to exit game.", GAME_SIZE.getGameAreaWidth() / 2 - 120, 20);
            }
        }

        private void drawLines(Graphics g) {
            Color[][] lines = opponentBoard.getLines();
            for (int i = 0; i < lines.length; i++) {
                for (int j = 0; j < lines[0].length; j++) {
                    Color color = lines[i][j];
                    if (color != null) {
                        drawBlockByGraphics(g, color, j * GAME_SIZE.getBlockCellSize(), i * GAME_SIZE.getBlockCellSize());
                    }
                }
            }
        }

        private static void drawBlockByGraphics(Graphics g, Color color, int x, int y) {
            g.setColor(color);
            g.fillRect(x, y, GAME_SIZE.getBlockCellSize(), GAME_SIZE.getBlockCellSize());
            g.setColor(Color.BLACK);
            g.drawRect(x, y, GAME_SIZE.getBlockCellSize(), GAME_SIZE.getBlockCellSize());
        }
    }

    private class OpponentGameInfoArea extends JPanel {
        public OpponentGameInfoArea() {
            setBackground(Color.BLACK);
            setForeground(Color.WHITE);
            setPreferredSize(new Dimension(GAME_SIZE.getInfoAreaWidth(), GAME_SIZE.getHeight()));
            setLayout(new GridLayout(0, 1));
            add(new MultiModeGameScreen.OpponentGameInfoArea.NextBlockArea());
            add(new MultiModeGameScreen.OpponentGameInfoArea.ScoreArea());
        }

        private class NextBlockArea extends JPanel {
            public NextBlockArea() {
                LineBorder border = (LineBorder) BorderFactory.createLineBorder(Color.DARK_GRAY, 5);
                setBorder(border);
                setBackground(Color.GRAY);
            }

            @Override
            public void paint(Graphics g) {
                super.paint(g);
                Block nextBlock = opponentBoard.getNextBlock();

                Graphics2D g2d = (Graphics2D) g;

                if (nextBlock != null) {
                    int[][] blockShape = nextBlock.getShape();
                    int blockWidth = nextBlock.getWidth();
                    int blockHeight = nextBlock.getHeight();
                    g2d.setColor(nextBlock.getBlockType().getColor());

                    int middleX = getWidth() / 2 - (blockWidth - 2) * GAME_SIZE.getBlockCellSize();
                    int middleY = GAME_SIZE.getInfoAreaWidth() / 2 - (blockHeight - 1) * GAME_SIZE.getBlockCellSize();
                    for (int i = 0; i < blockHeight; ++i) {
                        for (int j = 0; j < blockWidth; ++j) {
                            if (blockShape[i][j] == 1)
                                g2d.fillRect(middleX + j * GAME_SIZE.getBlockCellSize(), middleY + i * GAME_SIZE.getBlockCellSize(), GAME_SIZE.getBlockCellSize(), GAME_SIZE.getBlockCellSize());
                        }
                    }
                }
            }
        }

        private class ScoreArea extends JPanel {
            private final JLabel scoreLabel;
            private final JLabel levelLabel;

            public ScoreArea() {
                setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
                LineBorder border = (LineBorder) BorderFactory.createLineBorder(Color.DARK_GRAY, 5);
                setBorder(border);
                setBackground(Color.GRAY);

                levelLabel = new JLabel();
                scoreLabel = new JLabel();

                add(levelLabel);
                add(scoreLabel);

                levelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            }

            @Override
            public void paint(Graphics g) {
                super.paint(g);
                levelLabel.setText("LEVEL : " + opponentLevel);
                scoreLabel.setText("SCORE : " + opponentScore);
            }
        }
    }
}
