package component.menu;

public class ExitMenu extends AbstractMenu {

    public ExitMenu(String text) {
        super(text);
    }

    @Override
    public void setActionListener() {
        super.addActionListener(e -> System.exit(0));
    }
}