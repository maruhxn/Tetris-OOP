package screen;

import setting.GameSettings;
import setting.GameSize;

import javax.swing.*;
import java.awt.*;

public class GameClient extends JFrame {

    public static final int WINDOW_BORDER = 16;
    public static final int WINDOW_MANAGER_HEIGHT = 39;
    public static final GameSize gameSize = GameSettings.GAME_SIZE;

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
        setSize(gameSize.getWidth() + WINDOW_BORDER, gameSize.getHeight() + WINDOW_MANAGER_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setContentPane(new MainScreen());
        setVisible(true);
    }

    public void changeScreen(Screen screen) {
        setContentPane(screen);
        revalidate();
        repaint();
        requestFocus();
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
