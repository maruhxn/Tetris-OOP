package component.menu;

import screen.GameClient;

public class ScoreboardMenu extends AbstractMenu {
    public ScoreboardMenu(String text) {
        super(text);
    }

    @Override
    public void setActionListener() {
        super.addActionListener(e -> {
            GameClient client = (GameClient) extractClient();
            client.changeScreen(client.getScoreBoardScreen());
        });
    }
}