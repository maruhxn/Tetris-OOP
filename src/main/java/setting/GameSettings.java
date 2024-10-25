package setting;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class GameSettings {

    private static final Properties properties = new Properties();

    public static GameSize GAME_SIZE = GameSize.M;

    static {
        try (FileInputStream input = new FileInputStream("settings.properties")) {
            properties.load(input);
            loadGameSize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadGameSize() {
        GAME_SIZE = GameSize.valueOf(properties.getProperty("setting.game-size"));
    }

    public static void setGameSize(GameSize gameSize) {
        GAME_SIZE = gameSize;
        properties.setProperty("setting.game-size", gameSize.toString());
    }

    public static void saveSettings() {
        try (OutputStream output = new FileOutputStream("settings.properties")) {
            properties.store(output, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
