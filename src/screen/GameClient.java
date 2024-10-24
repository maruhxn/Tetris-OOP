package screen;

import javax.swing.*;
import java.awt.*;

import static setting.GameSettings.GAME_SIZE;

public class GameClient extends JFrame {

    public static final int WINDOW_BORDER = 16;
    public static final int WINDOW_MANAGER_HEIGHT = 39;

    private MainScreen mainScreen;
    private GameScreen gameScreen;
    private SettingScreen settingsScreen;
    private ScoreBoardScreen scoreBoardScreen;

    public GameClient() {
        super("TETRIS");
        setBackground(Color.BLACK);
        initScreens();
    }

    private void initScreens() {
        this.mainScreen = new MainScreen();
        this.gameScreen = new GameScreen();
        this.settingsScreen = new SettingScreen();
        this.scoreBoardScreen = new ScoreBoardScreen();
    }

    public void start() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setContentPane(mainScreen);

        // 먼저 창 크기를 자동으로 설정
        pack();

        // pack() 후에 정확한 Insets를 가져와 창 크기 조정
        Insets insets = getInsets();
        int totalWidth = GAME_SIZE.getWidth() + insets.left + insets.right;
        int totalHeight = GAME_SIZE.getHeight() + insets.top + insets.bottom;

        // Insets를 반영한 정확한 크기로 창 설정
        setSize(totalWidth, totalHeight);
        setLocationRelativeTo(null);
        revalidate();
        setVisible(true);
    }

    public void changeScreen(Screen screen) {
        setContentPane(screen);
        revalidate();
        repaint();
        screen.requestFocusInWindow();
    }

    public void startGame() {
        this.gameScreen.initGame();
        this.gameScreen.start();
    }

    public MainScreen getMainScreen() {
        return mainScreen;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public SettingScreen getSettingsScreen() {
        return settingsScreen;
    }

    public ScoreBoardScreen getScoreBoardScreen() {
        return scoreBoardScreen;
    }
}
