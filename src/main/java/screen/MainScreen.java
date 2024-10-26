package screen;

import component.menu.ExitMenu;
import component.menu.ScoreboardMenu;
import component.menu.SettingMenu;
import component.menu.StartMenu;
import setting.GameFont;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class MainScreen extends Screen {

    private final List<JButton> menus = new ArrayList<>();
    private int selectedIndex = 0;

    public MainScreen() {
        setLayout(new GridLayout(0, 1));

        initGameTitle();
        initMenuButtons();

        setKeyListener();

        setSelectedMenuColor(Color.WHITE, Color.BLACK);
    }

    private void initGameTitle() {
        JLabel gameTitle = new JLabel("TETRIS", SwingConstants.CENTER);
        gameTitle.setFont(GameFont.LARGE.getFont());
        gameTitle.setForeground(Color.white);
        add(gameTitle);
    }

    private void initMenuButtons() {
        menus.add(new StartMenu("GAME START"));
        menus.add(new SettingMenu("SETTING"));
        menus.add(new ScoreboardMenu("SCORE BOARD"));
        menus.add(new ExitMenu("EXIT"));

        for (JButton menu : this.menus) {
            add(menu);
        }
    }

    private void setKeyListener() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                setSelectedMenuColor(Color.BLACK, Color.WHITE);

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_DOWN -> selectedIndex = Math.min(menus.size() - 1, selectedIndex + 1);
                    case KeyEvent.VK_UP -> selectedIndex = Math.max(0, selectedIndex - 1);
                    case KeyEvent.VK_ENTER -> {
                        menus.get(selectedIndex).doClick();
                        removeKeyListener(this);
                    }
                }

                setSelectedMenuColor(Color.WHITE, Color.BLACK);
            }
        });
    }

    private void setSelectedMenuColor(Color bgColor, Color textColor) {
        menus.get(selectedIndex).setBackground(bgColor);
        menus.get(selectedIndex).setForeground(textColor);
    }
}
