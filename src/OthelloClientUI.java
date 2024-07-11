import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Stack;

public class OthelloClientUI extends JFrame implements ActionListener {
    private BoardPanel boardPanel;
    private OthelloClient othelloClient;
    private Play play;
    private final String name;
    private int myTurn;
    private JLabel whiteName;
    private JLabel blackName;
    private JButton whiteButton;
    private JButton blackButton;
    private boolean playing;
    private int[] score;
    private JTextField msgfield;
    private JTextArea msgarea;

    public OthelloClientUI(String name, String ip) {
        this.myTurn = 0;
        this.name = name;
        this.playing = false;
        othelloClient = new OthelloClient(this);
        othelloClient.start(ip, name);
        setTitle("Othello Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
        boardPanel = new BoardPanel(this);
        boardPanel.setBounds(50, 50, 560, 560);
        add(boardPanel);

        whiteButton = new JButton("White");
        whiteButton.addActionListener(this);
        whiteButton.setBounds(850, 100, 60, 50);
        add(whiteButton);

        blackButton = new JButton("Black");
        blackButton.addActionListener(this);
        blackButton.setBounds(850, 50, 60, 50);
        add(blackButton);

        JButton spectateButton = new JButton("Spectate");
        spectateButton.addActionListener(this);
        spectateButton.setBounds(770, 150, 80, 50);
        add(spectateButton);


        JButton playButton = new JButton("Play");
        playButton.addActionListener(this);
        playButton.setBounds(850, 150, 60, 50);
        add(playButton);

        whiteName = new JLabel("");
        whiteName.setBounds(720, 100, 130, 50);
        whiteName.setBackground(Color.WHITE);
        whiteName.setForeground(Color.BLACK);
        whiteName.setOpaque(true);
        add(whiteName);

        blackName = new JLabel("");
        blackName.setBackground(Color.BLACK);
        blackName.setOpaque(true);
        blackName.setForeground(Color.WHITE);
        blackName.setBounds(720, 50, 130, 50);
        add(blackName);

        msgarea = new JTextArea("");
        JScrollPane msgareaScrollPane = new JScrollPane(msgarea);
        msgareaScrollPane.setBounds(720,200,180,450);
        msgarea.setEditable(false);
        add(msgareaScrollPane);

        msgfield = new JTextField();
        msgfield.addActionListener(this);
        msgfield.setBounds(720,650,180,50);
        add(msgfield);

        setVisible(true);
    }

    public static void main(String[] args) {
        new OthelloClientUI(args[0], "localhost");
    }

    public void play() {
        playing = true;
        boardPanel.reset();
        play = new Play(myTurn);
        if (play.getTurn() == myTurn) {
            boardPanel.setAvailable(play.getAvailableMoves(), true);
        }

    }

    public void sendMsg()
    {
        othelloClient.sendMsg(new MessagePack(name, "msg: " + msgfield.getText()));
        msgfield.setText("");
    }

    public void sendSet(int x, int y) {
        othelloClient.sendMsg(new MessagePack(name, "set: " + x + y));
        System.out.println("x" + x + "y" + y);
    }

    public void sendRequest(String command) {
        if (command.equals("Black") && myTurn == 0) {
            othelloClient.sendMsg(new MessagePack(name, "reqturn: " + -1));
        } else if (command.equals("White") && myTurn == 0) {
            othelloClient.sendMsg(new MessagePack(name, "reqturn: " + 1));
        } else if (command.equals("Spectate")) {
            othelloClient.sendMsg(new MessagePack(name, "reqturn: " + 0));
        } else if (command.equals("Play") && (myTurn == 1 || myTurn == -1)) {
            othelloClient.sendMsg(new MessagePack(name, "reqplay: "));
        }
    }

    public void end() {
        playing = false;
        System.out.println("end");
        JOptionPane.showMessageDialog(this, "GAME OVER");
    }

    public void recieveSet(Go set) {
        if (set.x == -1) {
            System.out.println("skip");
            play.skip();
            if (play.getAvailableMoves().isEmpty()) {
                end();
                whiteButton.setText("White");
                blackButton.setText("Black");
                return;
            }
        } else {

            boardPanel.setAvailable(play.getAvailableMoves(), false);
            System.out.println(set);
            int turn = play.getTurn();
            boardPanel.placed(set.x, set.y, turn);
            boardPanel.flip(play.playon(set.x, set.y));
        }

         score = play.getScore();
        blackButton.setText(Integer.toString(score[0]));
        whiteButton.setText(Integer.toString(score[1]));

        if (play.getTurn() == myTurn) {
            boardPanel.setAvailable(play.getAvailableMoves(), true);
            if (play.getAvailableMoves().isEmpty()) {
                sendSet(-1,0);
            }
        }
    }

    public void receive(MessagePack msg) {
        if (msg.getMsg().startsWith("msg: ")) {
            System.out.println((msgarea.getText() + "\n" + msg.getName() + msg.getMsg().replaceFirst("msg: ", " : ")));
            msgarea.setText(msgarea.getText() + "\n" + msg.getName() + msg.getMsg().replaceFirst("msg: ", " : "));
        } else if (msg.getMsg().startsWith("set: ")) {
            int get = Integer.parseInt(msg.getMsg().replaceFirst("set: ", ""));
            Go move = new Go(get / 10, get % 10, 0, 0);
            recieveSet(move);
        } else if (msg.getMsg().startsWith("turn: ")) {
            int get = Integer.parseInt(msg.getMsg().replaceFirst("turn: ", ""));
            if (get == 0) {
                myTurn = 0;
                whiteName.setText("");
                blackName.setText("");
            }
            if (get == 1) {
                whiteName.setText(msg.getName());
            } else if (get == -1) {
                blackName.setText(msg.getName());
            }
            if (msg.getName().equals(name)) {
                System.out.println("MyTurn: " + get);
                myTurn = get;
            }
        } else if (msg.getMsg().startsWith("play: ")) {
            System.out.println("play");
            play();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof JButton) {
            String command = e.getActionCommand();
            if (!playing)
                sendRequest(command);
        }
        else{
            sendMsg();
        }
    }

}
