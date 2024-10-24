package setting;

import java.awt.event.KeyEvent;

public enum GameKey {
    ROTATE_KEY(KeyEvent.VK_SHIFT),
    PAUSE_KEY(KeyEvent.VK_P),
    MOVE_LEFT_KEY(KeyEvent.VK_LEFT),
    MOVE_RIGHT_KEY(KeyEvent.VK_RIGHT),
    MOVE_DOWN_KEY(KeyEvent.VK_DOWN),
    SUPER_DROP_KEY(KeyEvent.VK_SPACE),
    GAME_OVER_KEY(KeyEvent.VK_ESCAPE),
    ;

    private final int key;

    GameKey(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
