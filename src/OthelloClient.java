import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class OthelloClient {
    OthelloClientUI ui;
    ClientSender clientSender;

    public OthelloClient(OthelloClientUI ui) {
        this.ui = ui;

    }

    public void start(String ip, String name) {
        try {
            String serverIp = ip;
            Socket socket = new Socket(serverIp, 9999);
            System.out.println("Connected to " + serverIp);
            clientSender = new ClientSender(socket, name);
            Thread sender = new Thread(clientSender);
            Thread receiver = new Thread(new ClientReceiver(socket));
            sender.start();
            receiver.start();
        } catch (IOException e) {
        }
    }
    public void sendMsg(MessagePack msg)
    {
        clientSender.send(msg);
    }

    class ClientSender implements Runnable {
        Socket socket;
        ObjectOutputStream out;
        String name;

        public ClientSender(Socket socket, String name) throws IOException {
            this.socket = socket;
            this.name = name;
            out = new ObjectOutputStream(socket.getOutputStream());
        }

        public void run() {
            try {
                if (out != null) {
                    out.writeObject(new MessagePack(name,""));
                }
                while (out != null) {
                    sleep(50);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        public void send(MessagePack msg) {
            try {
                out.writeObject(msg);
            } catch (IOException e) {
            }
        }
    }

    class ClientReceiver implements Runnable {
        Socket socket;
        ObjectInputStream in;

        public ClientReceiver(Socket socket) {
            this.socket = socket;
            try {
                in = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
            }
        }

        public void run() {
            while (in != null) {

                try {
                    sleep(50);
                    readMessage(in.readObject());
                } catch (IOException | ClassNotFoundException e) {
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public void readMessage(Object msg) {
            if (msg instanceof MessagePack) {
                ui.receive((MessagePack) msg);
            }
        }
    }

    public String getTime() {
        SimpleDateFormat form = new SimpleDateFormat("[HH:mm:ss]");
        return (form.format(new Date()));
    }
    public static void main(String[] args) {
    }
}
