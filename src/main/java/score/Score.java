package score;

public class Score {

    private int score;
    private String username;

    public Score(String username, int score) {
        this.username = username;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public String getUsername() {
        return username;
    }
}
