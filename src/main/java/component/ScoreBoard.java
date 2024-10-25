package component;

import score.Score;
import score.ScoreDao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ScoreBoard extends JTable {
    private String[] columnNames = {"순위", "이름", "점수"};
    private ScoreDao scoreDao;
    private DefaultTableModel tableModel;
    private List<Score> scoreList;
    private JScrollPane scrollPane;
    private JViewport viewport;

    public ScoreBoard(ScoreDao scoreDao) {
        this.scoreDao = scoreDao;
        tableModel = new DefaultTableModel(columnNames, 0);

        setForeground(Color.WHITE);
        setBackground(Color.BLACK);
        setShowGrid(true);
        setModel(tableModel);


        scrollPane = new JScrollPane(this);
        scrollPane.setBorder(null);

        viewport = scrollPane.getViewport();
        viewport.setForeground(Color.WHITE);
        viewport.setBackground(Color.BLACK);
    }

    public void showTopScores() {
        tableModel.setRowCount(0);
        scoreList = scoreDao.getTopScores();

        for (int i = 0; i < scoreList.size(); ++i) {
            Score score = scoreList.get(i);
            Object[] record = new Object[3];
            record[0] = i + 1; // 순위
            record[1] = score.getUsername();
            record[2] = score.getScore();
            tableModel.addRow(record);
        }
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public List<Score> getScoreList() {
        return scoreList;
    }
}
