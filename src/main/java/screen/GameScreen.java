package screen;

import game.Board;
import game.EventHandler;
import game.block.Block;
import score.Score;
import score.ScoreDao;
import setting.GameFont;
import setting.GameKey;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static setting.Constants.SCORE_UNIT;
import static setting.GameSettings.GAME_SIZE;

public class GameScreen extends Screen {

    private BoardArea boardArea;
    private GameInfoArea gameInfoArea;
    private Timer timer;
    private Board board;
    private EventHandler eventHandler;
    private int score;
    private ScoreDao scoreDao;

    private boolean isPaused = false;

    public GameScreen(ScoreDao scoreDao) {
        this.scoreDao = scoreDao;
    }

    public void initGame() {
        initLayout();

        timer = new Timer(1000, e -> {
            if (!isPaused) triggerMoveBlock();
        });

        eventHandler = new EventHandler();
        board = new Board();

        setFocusable(true);
        setKeyListener();
        requestFocusInWindow();
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        boardArea = new BoardArea();
        gameInfoArea = new GameInfoArea();

        add(boardArea, BorderLayout.WEST);
        add(gameInfoArea, BorderLayout.EAST);

        boardArea.revalidate();
        gameInfoArea.revalidate();
    }

    private void triggerMoveBlock() {
        board.moveDown();
        int clearedLines = board.clearLines();
        addScore(clearedLines);

        if (board.checkGameOver()) {
            exitGame();
        }

        boardArea.repaint();
        gameInfoArea.repaint();
    }

    private void addScore(int lineCnt) {
        if (lineCnt <= 0) return;

        score += lineCnt * SCORE_UNIT;
    }

    public void start() {
        board.setUp();
        timer.start();
    }

    public void pause() {
        // 키 이벤트 멈춤
        resetKeyListeners();

        if (isPaused) { // 게임 재개
            continueGame();
        } else { // 게임 정지
            pauseGame();
        }

        isPaused = !isPaused;
        boardArea.repaint();
    }

    private void resetKeyListeners() {
        for (KeyListener kl : getKeyListeners()) {
            removeKeyListener(kl);
        }
    }

    public void continueGame() {
        setKeyListener();
        timer.restart();
    }

    public void pauseGame() {
        timer.stop();
        // p에만 반응하도록
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == GameKey.PAUSE_KEY.getKey()) {
                    pause();
                } else if (e.getKeyCode() == GameKey.GAME_OVER_KEY.getKey()) exitGame();
            }
        });
    }

    public void exitGame(
    ) {
        System.out.println("Game Over");

        if (timer.isRunning()) {
            timer.stop();
        }

        resetKeyListeners();

        // 현재 점수를 보여주고 닉네임을 입력받음
        String message = "GAME OVER!\nYour Score: " + score + "\nEnter your username:";
        String username = JOptionPane.showInputDialog(this, message, "Game Over", JOptionPane.PLAIN_MESSAGE);

        if (username != null && !username.trim().isEmpty()) {
            // 입력된 닉네임과 점수를 저장하거나 랭킹에 추가
            Score scoreEntity = new Score(username, score);
            scoreDao.save(scoreEntity);
        }

        GameClient client = (GameClient) getTopLevelAncestor();
        client.goToScoreboardScreen();
    }

    public void showPausePane() {
    }

    public void showNicknameInputPane() {
    }

    private void setKeyListener() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == GameKey.MOVE_LEFT_KEY.getKey()) board.moveLeft();
                else if (e.getKeyCode() == GameKey.MOVE_RIGHT_KEY.getKey()) board.moveRight();
                else if (e.getKeyCode() == GameKey.MOVE_DOWN_KEY.getKey()) board.moveDown();
//                else if (e.getKeyCode() == GameKey.SUPER_DROP_KEY.getKey()) board.getCurrBlock().superDrop();
                else if (e.getKeyCode() == GameKey.ROTATE_KEY.getKey()) board.rotateBlock();
                else if (e.getKeyCode() == GameKey.PAUSE_KEY.getKey()) pause();

                boardArea.repaint();
            }
        });
    }

    public class BoardArea extends JPanel {

        public BoardArea() {
            setBackground(Color.BLACK);
            setForeground(Color.WHITE);
            setPreferredSize(new Dimension(GAME_SIZE.getGameAreaWidth(), GAME_SIZE.getHeight()));
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            drawLines(g);
            drawBlock(g);
        }

        private void drawBlock(Graphics g) {
            Block currBlock = board.getCurrBlock();
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
            Color[][] lines = board.getLines();
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

    private class GameInfoArea extends JPanel {
        public GameInfoArea() {
            setBackground(Color.BLACK);
            setForeground(Color.WHITE);
            setPreferredSize(new Dimension(GAME_SIZE.getInfoAreaWidth(), GAME_SIZE.getHeight()));
            setLayout(new GridLayout(0, 1));
            add(new NextBlockArea());
            add(new ScoreArea());
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
                Block nextBlock = board.getNextBlock();

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
                scoreLabel = new JLabel("0");

                add(levelLabel);
                add(scoreLabel);

                levelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            }

            @Override
            public void paint(Graphics g) {
                super.paint(g);
//            levelLabel.setText("LEVEL : " + GameManager.getLevel());
                scoreLabel.setText("SCORE : " + score);
            }
        }
    }

}
