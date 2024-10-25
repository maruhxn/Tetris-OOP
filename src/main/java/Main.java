import score.ScoreDao;
import screen.GameClient;

public class Main {
    public static void main(String[] args) {
        ScoreDao scoreDao = new ScoreDao();
        GameClient client = new GameClient(scoreDao);
        client.start();
    }
}