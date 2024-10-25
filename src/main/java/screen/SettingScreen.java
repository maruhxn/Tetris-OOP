package screen;

import component.Button;
import setting.GameFont;
import setting.GameSettings;
import setting.GameSize;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static setting.GameSettings.GAME_SIZE;
import static setting.GameSettings.setGameSize;
import static util.Utility.addPadding;

public class SettingScreen extends Screen {

    private SizeControlArea sizeControlArea;
    private NavArea navArea;

    private GameSize selectedSize;

    public SettingScreen() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        sizeControlArea = new SizeControlArea();
        navArea = new NavArea();

        add(sizeControlArea);
        add(navArea);
    }

    public void selectBoardSize(GameSize gameSize) {

    }

    public void saveSettings() {
        GameSettings.saveSettings();
        sizeControlArea.resizeClient();
    }

    public void displayConfirmation() {

    }

    private class SizeControlArea extends JPanel {
        JLabel sizeControlLabel;
        JPanel btnArea;
        List<Button> sizeBtns = new ArrayList<>();

        public SizeControlArea() {
            init();
            initButtonArea();
            selectAndHighlightBtn(GAME_SIZE);
            addPadding(sizeControlLabel, 10, 10);

            add(sizeControlLabel);
            add(btnArea);
        }

        private void initButtonArea() {
            btnArea = new JPanel();

            btnArea.setLayout(new GridLayout(1, 0));
            btnArea.setBackground(Color.BLACK);
            btnArea.setForeground(Color.WHITE);

            sizeBtns.add(new SizeBtn("SMALL", GameSize.S));
            sizeBtns.add(new SizeBtn("MEDIUM", GameSize.M));
            sizeBtns.add(new SizeBtn("LARGE", GameSize.L));

            for (JButton btn : sizeBtns) {
                btnArea.add(btn);
            }

            addPadding(btnArea, 20, 20);
        }

        private void init() {
            setBackground(Color.BLACK);
            setForeground(Color.WHITE);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            sizeControlLabel = new JLabel("화면 크기 조절");
            sizeControlLabel.setFont(GameFont.MEDIUM.getFont());
            sizeControlLabel.setForeground(Color.WHITE);
            sizeControlLabel.setBackground(Color.BLACK);
            sizeControlLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        private void resizeClient() {
            GameClient client = (GameClient) getTopLevelAncestor();
            client.resize();
        }

        private class SizeBtn extends Button {
            public SizeBtn(String text, GameSize gameSize) {
                super(text);
                addActionListener(e -> selectAndHighlightBtn(gameSize));
            }
        }

        private void selectAndHighlightBtn(GameSize gameSize) {
            selectedSize = gameSize;
            setGameSize(gameSize);
            for (int i = 0; i < sizeBtns.size(); ++i) {
                if (i == gameSize.getId()) {
                    sizeBtns.get(i).setBackground(Color.YELLOW);
                } else {
                    sizeBtns.get(i).setBackground(Color.WHITE);
                }
            }
        }
    }

    private class NavArea extends JPanel {
        private JButton mainBtn, saveBtn;

        public NavArea() {
            setBackground(Color.BLACK);
            setForeground(Color.WHITE);
            mainBtn = new MainBtn("메인으로");
            saveBtn = new SaveBtn("설정 저장");

            add(mainBtn);
            add(saveBtn);
        }

        private class MainBtn extends Button {
            public MainBtn(String text) {
                super(text);
                addActionListener(e -> {
                    GameClient client = (GameClient) getTopLevelAncestor();
                    client.goToMainScreen();
                });
            }
        }

        private class SaveBtn extends Button {
            public SaveBtn(String text) {
                super(text);
                addActionListener(e -> saveSettings());
            }
        }
    }
}
