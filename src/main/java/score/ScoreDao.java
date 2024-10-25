package score;

import setting.GameSettings;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreDao {

    private final String url;
    private final String username;
    private final String password;

    public ScoreDao() {
        this.url = GameSettings.getUrl();
        this.username = GameSettings.getUsername();
        this.password = GameSettings.getPassword();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            createTableIfNotExists();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void createTableIfNotExists() {
        String checkTableQuery = "SHOW TABLES LIKE 'scores'";
        String createTableQuery = """
                CREATE TABLE scores (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(50) NOT NULL,
                    score INT NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;

        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkTableQuery)) {

            if (!rs.next()) {  // 테이블이 존재하지 않을 경우
                stmt.execute(createTableQuery);
                System.out.println("scores 테이블이 생성되었습니다.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Score> getTopScores() {
        List<Score> topScores = new ArrayList<>();
        String query = "SELECT * FROM scores ORDER BY score DESC LIMIT 10";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String username = rs.getString("username");
                int score = rs.getInt("score");
                topScores.add(new Score(username, score));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return topScores;
    }

    public void save(Score score) {
        String query = "INSERT INTO scores (username, score) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, score.getUsername());
            stmt.setInt(2, score.getScore());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
