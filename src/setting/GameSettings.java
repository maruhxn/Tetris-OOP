package setting;

public class GameSettings {

    public static GameSize GAME_SIZE = GameSize.M;

    public static void setGameSize(GameSize gameSize) {
        GAME_SIZE = gameSize;
    }

    public void saveSettings() {

    }

}
