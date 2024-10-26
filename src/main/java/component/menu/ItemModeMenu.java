package component.menu;

import screen.GameClient;

public class ItemModeMenu extends AbstractMenu {
    public ItemModeMenu(String text) {
        super(text);
    }

    @Override
    public void setActionListener() {
        super.addActionListener(e -> {
            GameClient client = extractClient();
            client.startItemMode();
        });
    }
}
