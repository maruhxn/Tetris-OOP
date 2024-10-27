package component.menu;

import screen.GameClient;

public class MultiPlayModeMenu extends AbstractMenu {
    public MultiPlayModeMenu(String text) {
        super(text);
    }

    @Override
    public void setActionListener() {
        super.addActionListener(e -> {
            GameClient client = extractClient();
            client.startMultiPlayMode();
        });
    }
}
