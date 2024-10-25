package component;

import javax.swing.*;
import java.awt.*;

public class Button extends JButton {
    public Button(String text) {
        super(text);
        setOpaque(true);  // 배경색을 보이도록 설정
        setBorderPainted(false);  // 버튼 테두리를 없애기 (옵션)
        setBackground(Color.white);
        setForeground(Color.black);
    }
}
