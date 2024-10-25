package setting;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class GameSettings {

    private static final Properties properties = new Properties();

    public static GameSize GAME_SIZE = GameSize.M;

    static {
        try (FileInputStream input = new FileInputStream("settings.properties")) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setGameSize(GameSize gameSize) {
        GAME_SIZE = gameSize;
    }

    public void saveSettings() {

    }

    public static String getUrl() {
        return properties.getProperty("db.url");
    }

    public static String getUsername() {
        return properties.getProperty("db.username");
    }

    public static String getPassword() {
        return properties.getProperty("db.password");
    }

}
