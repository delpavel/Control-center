package autoExchange.delete;


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


public class SocketDispatcher extends Server implements Runnable {

    private Socket client;
    private BufferedReader bufferedReader;
    private ArrayList<String> computers = new ArrayList<>();

    public SocketDispatcher(Socket client) {
        this.client = client;

    }

    @Override
    public void run() {

       /* try {
            BufferedReader socketInputStream = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter socketOutputStream = new PrintWriter(client.getOutputStream(), true);
            String input;
            while ((input = socketInputStream.readLine()) != null) {

                if (input.equals("getFileAutoExchange")) {
                    socketOutputStream.println("ok");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        for (File file : files) {
            addComp(file);
        }

        sendFile();

        try {
            bufferedReader.close();
            computers.clear();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.gc();

    }

    private void addComp(File file) {

        if (file.exists()) {
            try {
                bufferedReader = new BufferedReader(new FileReader(file));
                String input;
                while ((input = bufferedReader.readLine()) != null) {
                    if (input.contains("ComputerName")) {
                        computers.add(input.substring(17, input.indexOf("\"}")));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    private void sendFile() {


        try (PrintWriter printWriter = new PrintWriter(client.getOutputStream())) {


            for (String computer : computers) {
                printWriter.println(computer);
            }

            printWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



