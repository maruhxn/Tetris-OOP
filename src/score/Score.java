package score;

public class Score {

    private int score;

    private final int SCORE_UNIT = 100;

    public void addScore(int lineCnt) {
        if (lineCnt <= 0) return;

        score += lineCnt * SCORE_UNIT;
    }

    public void saveScore(String username) {

    }

    public int getScore() {
        return score;
    }
}
