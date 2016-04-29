package autoExchange.model.radmin;


import autoExchange.view.Message;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class Radmin {

    private String ip;
    private File startVbsFile;
    private ArrayList<String> problemConnection = new ArrayList<>();

    public Radmin() {

        problemConnection.add("192.168.174.111");
        problemConnection.add("192.168.174.70");//Брест, Варшавское шоссе, 11
        problemConnection.add("192.168.174.1");//Орша, Чернышевского 4-й пер., 4
        problemConnection.add("192.168.174.9");//Минск, ул. Алибегова, 13, корп.1
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    private void runVbs() {

        Process p = null;
        try {
            p = Runtime.getRuntime().exec("cmd /c " + ip + ".vbs");
        } catch (IOException e) {
            e.printStackTrace();
        }
        startVbsFile = new File(ip + ".vbs");
        if (startVbsFile.exists()) {
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void run(String computer) {

        createVbsFile(computer);
        runVbs();
        if (startVbsFile.exists()) {
            startVbsFile.delete();
        }
    }

    private void createVbsFile(String computer) {

        try {
            FileWriter writer = new FileWriter(ip + ".vbs", false);

            if (computer.toLowerCase().contains("server")) {
                if (problemConnection.contains(ip)) {
                    writer.write(bySysadminGetServer());
                } else {
                    writer.write(getServer());
                }
            } else {
                if (problemConnection.contains(ip)) {
                    Message.printMessage("Не могу подключитсья к " + computer + "! Используйте radmin для подключения через it-az", "Ошибка");
                } else {
                    writer.write(getOther(computer));
                }
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private String getServer() {

        return "set WshShell = WScript.CreateObject(\"WScript.Shell\")\n" +
                "obj = WshShell.Run (\"\"\"c:\\Program Files (x86)\\Radmin Viewer 3\\radmin.exe\"\" /connect:" + ip + ":4899 /updates:10\",0)\n" +
                "WScript.Sleep 4000\n" +
                "if WshShell.AppActivate(\"Система безопасности Radmin: \" + ip + \"\") then\n" +
                "WScript.Sleep 500\n" +
                "WshShell.SendKeys \"administrator\"\n" +
                "WshShell.SendKeys \"{TAB}\"\n" +
                "WshShell.SendKeys \"!Interfarmax!\"\n" +
                "WshShell.SendKeys \"{ENTER}\"\n" +
                "end if";
    }

    private String getOther(String computer) {

        return "set WshShell = WScript.CreateObject(\"WScript.Shell\")\n" +
                "obj = WshShell.Run (\"\"\"c:\\Program Files (x86)\\Radmin Viewer 3\\radmin.exe\"\" /connect:" + computer + ":4899 /through:" + ip + ":4899 /updates:10\",0)\n" +
                "WScript.Sleep 4000\n" +
                "if WshShell.AppActivate(\"Система безопасности Radmin: " + ip + "\") then\n" +
                "WScript.Sleep 500\n" +
                "WshShell.SendKeys \"administrator\"\n" +
                "WshShell.SendKeys \"{TAB}\"\n" +
                "WshShell.SendKeys \"!Interfarmax!\"\n" +
                "WshShell.SendKeys \"{ENTER}\"\n" +
                "end if\n" +
                "WScript.Sleep 4000\n" +
                "WshShell.AppActivate \"Система безопасности Radmin: " + computer + "\"\n" +
                "WScript.Sleep 500\n" +
                "WshShell.SendKeys \"administrator\"\n" +
                "WshShell.SendKeys \"{TAB}\"\n" +
                "WshShell.SendKeys \"!Interfarmax!\"\n" +
                "WshShell.SendKeys \"{ENTER}\"";
    }

    private String bySysadminGetServer() {

        return "set WshShell = WScript.CreateObject(\"WScript.Shell\")\n" +
                "obj = WshShell.Run (\"\"\"c:\\Program Files (x86)\\Radmin Viewer 3\\radmin.exe\"\" /connect:" + ip + ":4899 /through:it-az:4899 /updates:10\",0)\n" +
                "WScript.Sleep 4000\n" +
                "if WshShell.AppActivate(\"Система безопасности Radmin: it-az\") then\n" +
                "WScript.Sleep 500\n" +
                "WshShell.SendKeys \"administrator\"\n" +
                "WshShell.SendKeys \"{TAB}\"\n" +
                "WshShell.SendKeys \"!Interfarmax!\"\n" +
                "WshShell.SendKeys \"{ENTER}\"\n" +
                "end if\n" +
                "WScript.Sleep 4000\n" +
                "WshShell.AppActivate \"Система безопасности Radmin: " + ip + "\"\n" +
                "WScript.Sleep 500\n" +
                "WshShell.SendKeys \"administrator\"\n" +
                "WshShell.SendKeys \"{TAB}\"\n" +
                "WshShell.SendKeys \"!Interfarmax!\"\n" +
                "WshShell.SendKeys \"{ENTER}\"";
    }


    public Set getComputersName(String ip) {

        Set<String> nameComputer = new HashSet<>();
        Socket socket;

        try {
            //socket = new Socket(InetAddress.getByName("10.13.211.59"), 4444);
            socket = new Socket(InetAddress.getByName(ip), 4444);
        } catch (IOException e) {
            Message.printMessage("Не могу подключитсья к " + ip + "! Возможно не установлен сервер java или нет VPN канала на ТО!", "Ошибка");
            return null;
        }
        BufferedReader socketInputStream;
        try {
            socketInputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String input;
            while ((input = socketInputStream.readLine()) != null) {
                nameComputer.add(input);
            }
            socketInputStream.close();
            socket.close();
        } catch (IOException e) {
            Message.printMessage("Не могу получить socket по " + ip + "! Обратитесь к разработчику ПО!", "Ошибка");
            return null;
        }


        return nameComputer;
    }


    //РАЗРАБОТКА КОДА НИЖЕ ОТКЛАДЫВАЕТСЯ
////////////////////////////////////////////////////////////////////
   /* public void getFileAutoExchangeFromPharmacy(String ip) throws IOException {

        Socket socket = null;
        try {
            socket = new Socket(InetAddress.getByName(ip), 4444);
        } catch (IOException e) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Message.printMessage("Не могу подлкючиться к серверу!", "Ошибка");
                }
            });
            return;
        }

        BufferedReader socketInputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter socketOutputStream = new PrintWriter(socket.getOutputStream(), true);


        socketOutputStream.println("getFileAutoExchange");
        String input;
        try {
            while ((input = socketInputStream.readLine()) != null) {
                if (input.equals("ok")) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Message.printMessage("Файл получен!", "Информация");
                        }
                    });

                }
            }
            socket.close();
        } catch (IOException e) {
            Message.printMessage("Не могу подлкючиться к серверу!", "Ошибка");
        }


    }*/
//////////////////////////////////////////////////////////////////////////////////

}
