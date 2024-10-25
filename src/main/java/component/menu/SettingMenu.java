package component.menu;

import screen.GameClient;

public class SettingMenu extends AbstractMenu {
    public SettingMenu(String text) {
        super(text);
    }

    @Override
    public void setActionListener() {
        super.addActionListener(e -> {
            GameClient client = (GameClient) extractClient();
            client.changeScreen(client.getSettingsScreen());
        });
    }
}