package autoExchange.model;


import autoExchange.view.Message;

import java.io.*;

public class AutoExchange {

    private final String pathFileStartBat = new File(".").getAbsolutePath().toString();
    private final String patchCpFile = "\\\\ts1\\Interfarmax\\BaseRozn\\CP\\";
    private final String pathAutoExchange = "\\\\fileserver\\NEW_USERS\\!Общий_Обмен\\auto.prm";
    private OutputStream outputStream;
    private DataReader dataReader;


    public void setDataReader(DataReader dataReader) {
        this.dataReader = dataReader;
    }

    public void writeFileAutoExchange(String option, String kod) {
        String text;
        if (dataReader.getIncorrectAutoExchangeFileName().containsKey(kod)) {
            text = "[General]\nAutoexchange=1\nQuit=1;\n[Autoexchange]\nSharedMode=1;\n" + option + dataReader.getIncorrectAutoExchangeFileName().get(kod) + ";\n";
        } else if (Integer.parseInt(kod) < 100) {
            text = "[General]\nAutoexchange=1\nQuit=1;\n[Autoexchange]\nSharedMode=1;\n" + option + "П" + kod + ";\n";
        } else {
            text = "[General]\nAutoexchange=1\nQuit=1;\n[Autoexchange]\nSharedMode=1;\n" + option + kod + ";\n";
        }
        try {
            outputStream = new FileOutputStream(pathAutoExchange);
        } catch (FileNotFoundException e) {
            Message.printMessage(pathAutoExchange + " - ошибка чтения autp.prm!", "Ошибка");
        }
        try {
            outputStream.write(text.getBytes());
        } catch (IOException e) {
            Message.printMessage(pathAutoExchange + " - ошибка записи autp.prm!", "Ошибка");
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            Message.printMessage(pathAutoExchange + " - ошибка закрытия autp.prm!", "Ошибка");
        }
    }


    public void runBat(String option, String kod) {

        boolean runStartbat = true;
        try {
            Process p = Runtime.getRuntime().exec("cmd /c " + pathFileStartBat + "\\start.bat");
            File startbatFile = new File(pathFileStartBat + "\\start.bat");
            if (startbatFile.exists()) {

                long timeBefore = System.currentTimeMillis();
                p.waitFor();
                long timeNow = System.currentTimeMillis();
                if (timeNow - timeBefore < 5000) {
                    Message.printMessage("Конфигуратор занят, попробуйте позже!", "Ошибка");
                    runStartbat = false;
                }

            } else {
                throw new FileNotFoundException();
            }

            if (option.equals("Выгрузка") && runStartbat) {
                sendFtpFiles(kod);
            }

        } catch (IOException e) {
            Message.printMessage("Не найден start.bat файл! Файл start.bat должен нахиодиться рядом с запускным файлом программы!", "Ошибка");
        } catch (InterruptedException e) {
            Message.printMessage("Блокировка при чтении start.bat файла!", "Ошибка");
        }

    }

    private void sendFtpFiles(String kod) {

        String patchFull;
        String fileAutoExchange;
        if (Integer.parseInt(kod) <= 100 || Integer.parseInt(kod) == 141 || Integer.parseInt(kod) == 142) {
            if (dataReader.getIncorrectAutoExchangeFileName().containsKey(kod)) {
                fileAutoExchange = "P" + dataReader.getIncorrectAutoExchangeFileName().get(kod).substring(1) + "8.zip";
                patchFull = patchCpFile + fileAutoExchange;
            } else {
                fileAutoExchange = "P" + kod + "8.zip";
                patchFull = patchCpFile + fileAutoExchange;
            }
        } else {
            fileAutoExchange = kod + "0.zip";
            patchFull = patchCpFile + fileAutoExchange;
        }

        FtpSender ftpClient = new FtpSender(kod, patchFull, fileAutoExchange);
        ftpClient.sendFtpServer();
    }

}
