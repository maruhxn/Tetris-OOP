package component.menu;

import screen.GameClient;
import setting.GameFont;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractMenu extends JButton {

    public AbstractMenu(String text) {
        super(text);
        setBackground(Color.BLACK);
        setForeground(Color.white);
        setFont(GameFont.SMALL.getFont());
        setActionListener();
    }

    public abstract void setActionListener();

    public GameClient extractClient() {
        return (GameClient) this.getTopLevelAncestor();
    }
}