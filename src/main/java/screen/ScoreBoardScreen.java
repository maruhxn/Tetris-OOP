package screen;

import component.Button;
import component.ScoreBoard;
import score.ScoreDao;
import setting.GameFont;

import javax.swing.*;
import java.awt.*;

import static util.Utility.addPadding;

public class ScoreBoardScreen extends Screen {

    private JLabel label;
    private ScoreBoard scoreboard;
    private NavArea navArea;

    public ScoreBoardScreen(ScoreDao scoreDao) {

        setLayout(new BorderLayout());
        // LABEL
        label = new JLabel("Score Board", SwingConstants.CENTER);
        label.setBackground(Color.BLACK);
        label.setForeground(Color.WHITE);
        label.setFont(GameFont.LARGE.getFont());

        // SCOREBOARD
        scoreboard = new ScoreBoard(scoreDao);
        scoreboard.showTopScores();

        addPadding(label, 10, 10);
        addPadding(scoreboard, 10, 10);

        navArea = new NavArea();

        add(label, BorderLayout.NORTH);
        add(scoreboard.getScrollPane(), BorderLayout.CENTER);
        add(navArea, BorderLayout.SOUTH);
    }

    public void updateScoreBoard() {
        scoreboard.showTopScores();
    }

    private static class NavArea extends JPanel {
        private Button homeBtn;

        public NavArea() {
            homeBtn = new Button("메인으로");

            homeBtn.addActionListener(e -> {
                GameClient client = (GameClient) getTopLevelAncestor();
                client.changeScreen(client.getMainScreen());
            });

            add(homeBtn);
        }

    }
}
