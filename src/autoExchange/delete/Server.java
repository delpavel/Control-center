package autoExchange.delete;


import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server {

    protected ArrayList<File> files = new ArrayList<>();

    public Server() {

        files.add(new File("c:\\links.tmp"));
        files.add(new File("c:\\linkskosm.tmp"));
        files.add(new File("c:\\linksopt.tmp"));
        files.add(new File("c:\\links102.tmp"));
        files.add(new File("c:\\links103.tmp"));
        files.add(new File("c:\\links104.tmp"));
        files.add(new File("c:\\links105.tmp"));
        files.add(new File("c:\\links106.tmp"));
        files.add(new File("c:\\links108.tmp"));
    }

    private static void runServer() {

        ServerSocket server = null;
        Socket client = null;

        try {
            server = new ServerSocket(4444);
        } catch (IOException e) {
            System.exit(-1);
        }

        while (true) {
            try {
                client = server.accept();
            } catch (IOException e) {
                System.exit(-1);
            }

            new Thread(new SocketDispatcher(client)).start();

        }

    }


    public static void main(String[] args) {
        runServer();
    }
}
