import java.io.*;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.*;
import java.net.*;

import static java.lang.Thread.sleep;

public class OthelloServer {
    HashMap<String, ObjectOutputStream> clients;
    boolean white;
    boolean black;

    public OthelloServer() {
        clients = new HashMap();
        Collections.synchronizedMap(clients);
        white = false;
        black = false;
    }

    public void start() {
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(9999);
            System.out.println("Server started");
            while (true) {
                socket = serverSocket.accept();
                Thread thread = new Thread(new ServerReciever(socket));
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void sendToAll(MessagePack msg) {
        Iterator<String> it = clients.keySet().iterator();
        while (it.hasNext()) {
            ObjectOutputStream oos;
            oos = clients.get(it.next());
            try {
                oos.writeObject(msg);
            } catch (Exception e) {
                throw new RuntimeException();
            }

        }
    }

    class ServerReciever implements Runnable {
        Socket socket;
        ObjectInputStream in;
        ObjectOutputStream out;

        ServerReciever(Socket socket) {
            this.socket = socket;
            try {
                in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {
            }
        }

        public void run() {
            String name = "";
            MessagePack msg;
            try {
                name = ((MessagePack) in.readObject()).getName();
                clients.put(name, out);
                white = false;
                black = false;
                sendToAll(new MessagePack("Admin", "turn: 0"));
                System.out.println(name + " connected");
                while (in != null) {
                    sleep(50);
                    Object o = in.readObject();
                    if (o instanceof MessagePack) {
                        msg = (MessagePack) o;
                        if (msg.getMsg().startsWith("reqturn: ")) {
                            System.out.println(msg.getMsg());
                            int get = Integer.parseInt(msg.getMsg().replaceFirst("reqturn: ", ""));
                            if (get == 1 && !white) {
                                white = true;
                                sendToAll(new MessagePack(msg.getName(), "turn: 1"));
                            } else if (get == -1 && !black) {
                                black = true;
                                sendToAll(new MessagePack(msg.getName(), "turn: -1"));
                            } else if (get == 0) {
                                white = false;
                                black = false;
                                sendToAll(new MessagePack("Admin", "turn: 0"));
                            }
                        } else if (msg.getMsg().startsWith("reqplay: ")) {
                            if(white && black)
                            {
                                sendToAll(new MessagePack(msg.getName(), "play: "));
                            }
                        }else
                            sendToAll(msg);
                    }

                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                clients.remove(name);
                System.out.println(name + " disconnected");
            }
        }
    }

    public static void main(String[] args) {
        new OthelloServer().start();
    }
}
