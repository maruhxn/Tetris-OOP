package setting;

import java.awt.*;

public enum GameFont {
    SMALL(new Font("Courier", Font.BOLD, 12)),
    MEDIUM(new Font("Courier", Font.BOLD, 25)),
    LARGE(new Font("Courier", Font.BOLD, 25)),
    ;

    private final Font font;

    GameFont(Font font) {
        this.font = font;
    }

    public Font getFont() {
        return font;
    }
}
