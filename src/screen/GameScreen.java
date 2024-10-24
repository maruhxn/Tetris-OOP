package screen;

import game.Board;
import game.EventHandler;
import game.block.Block;
import score.Score;
import setting.GameKey;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static setting.GameSettings.GAME_SIZE;

public class GameScreen extends Screen {

    private BoardArea boardArea;
    private GameInfoArea gameInfoArea;
    private Timer timer;
    private Board board;
    private EventHandler eventHandler;
    private Score score;

    public void initGame() {
        initLayout();

        timer = new Timer(1000, e -> {
            triggerMoveBlock();
        });

        eventHandler = new EventHandler();
        score = new Score();
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
        score.addScore(clearedLines);
        boardArea.repaint();
        gameInfoArea.repaint();
    }

    public void start() {
        board.setUp();
        timer.start();
    }

    public void pause() {
    }

    public void continueGame() {
    }

    public void exitGame() {
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
//                else if (e.getKeyCode() == GameKey.PAUSE_KEY.getKey()) pause();
//                else if (e.getKeyCode() == GameKey.GAME_OVER_KEY.getKey()) gameOver();

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
//            g.setColor(Color.WHITE);
//            g.setFont(GameFont.SMALL.getFont());
//            g.drawString(((GameScreen) getParent()).getStatus() ? "PAUSE!!" : "PLAYING!", GAME_SIZE.getGameAreaWidth() / 2 - 20, 20);
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
                scoreLabel.setText("SCORE : " + score.getScore());
            }
        }
    }

}
