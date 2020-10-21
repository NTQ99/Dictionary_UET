package vnu.edu.vn;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class DictionaryApplication {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        JFrame frame = new JFrame("UET Dictionary");
        frame.setIconImage(Toolkit.getDefaultToolkit().createImage("icon/icon.png"));
        frame.setContentPane(new DictionaryGUI().getMain());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        //frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

