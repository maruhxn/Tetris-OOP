package screen;

import javax.swing.*;
import java.awt.*;

public abstract class Screen extends JPanel {
    public Screen() {
        setFocusable(true);
        setBackground(Color.BLACK);
        setForeground(Color.white);
    }
}
