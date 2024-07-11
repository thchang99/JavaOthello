import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OthelloConnect extends JFrame implements ActionListener {
    JTextField name;
    JTextField ip;
    public OthelloConnect() {
        setTitle("Othello Connect");
        setSize(200,200);
        setLayout(new GridLayout(3,1));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        name = new JTextField("name");
        add(name);
        ip = new JTextField("ip");
        add(ip);
        JButton login = new JButton("Login");
        login.addActionListener(this);
        add(login);
        setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {
        new OthelloClientUI(name.getText(), ip.getText());
        setVisible(false);
    }

    public static void main(String[] args) {
        new OthelloConnect();
    }
}
