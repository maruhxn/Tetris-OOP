package screen;

import score.ScoreDao;

public class ScoreBoardScreen extends Screen {

    private final ScoreDao scoreDao;

    public ScoreBoardScreen(ScoreDao scoreDao) {
        this.scoreDao = scoreDao;
    }

    public void showTopScores() {

    }
}
