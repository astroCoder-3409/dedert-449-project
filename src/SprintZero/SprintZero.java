package SprintZero;
import java.awt.*;
import java.io.*;
import javax.swing.*;

class GameBoard extends JPanel {

    public void paintComponent(Graphics g)
    {
        g.setColor(new Color(0, 0, 0));
        g.drawLine(220, 220, 280, 220);
        g.drawLine(220, 240, 280, 240);
        g.drawLine(240, 200, 240, 260);
        g.drawLine(260, 200, 260, 260);
    }
}

public class SprintZero {
    public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        JButton button = new JButton("Test Button");
        JLabel text = new JLabel( "^^^^^ Click Above ^^^^^", JLabel.CENTER);
        GameBoard panel = new GameBoard();
        JCheckBox checkbox = new JCheckBox("Test Checkbox");
        JRadioButton rButton1 = new JRadioButton("Option 1");
        JRadioButton rButton2 = new JRadioButton("Option 2");
        button.setBounds(150, 40, 220, 50);
        rButton1.setBounds(150, 300, 220, 30);
        rButton2.setBounds(150, 330, 220, 30);
        text.setBounds(150, 100, 220, 10);
        checkbox.setBounds(150, 120, 220, 20);
        panel.add(button);
        panel.add(rButton1);
        panel.add(rButton2);
        panel.add(text);
        panel.add(checkbox);
        frame.setSize(500, 600);
        frame.setContentPane(panel);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    public static boolean isBinaryFormPalindrome(int x) {
        String s = Integer.toBinaryString(x);
        for (int i = 0; i < s.length()/2; i++) {
            if (s.charAt(i) != s.charAt(s.length()-1-i)) return false;
        }
        return true;
    }

    public static boolean isAscending(int[] list) {
        for (int i = 1; i < list.length; i++) {
            if (list[i-1] >= list[i]) return false;
        }
        return true;
    }
}
