package frontend;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame{
    public  GUI(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,600);
        setVisible(true);
        setLayout(new FlowLayout());
        JPanel panel = new JPanel();
    }
}
