package component.menu;

import screen.GameClient;

public class StartMenu extends AbstractMenu {
    public StartMenu(String text) {
        super(text);
    }

    @Override
    public void setActionListener() {
        super.addActionListener(e -> {
            GameClient client = extractClient();
            client.startGame();
        });
    }
}
