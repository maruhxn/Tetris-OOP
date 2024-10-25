package util;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public class Utility {

    public static void addPadding(JComponent component, int x, int y) {
        Border border = component.getBorder();
        Border margin = new EmptyBorder(y, x, y, x);
        component.setBorder(new CompoundBorder(border, margin));
    }
}
