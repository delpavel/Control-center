package autoExchange.model;


import autoExchange.view.Message;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class FtpSender {

    private String kod;
    private String patchFile;
    private String fileAutoExchange;

    public FtpSender(String kod, String patchFile, String fileAutoExchange) {
        this.kod = kod;
        this.patchFile = patchFile;
        this.fileAutoExchange = fileAutoExchange;
    }

    public void sendFtpServer() {
        String server = "10.12.1.8";
        int port = 21;
        String user = "helpdesk";
        String pass = "helpdesk";
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                Message.printMessage("Ошибка сервера: " + replyCode, "Ошибка");
                return;
            }
            boolean success = ftpClient.login(user, pass);
            if (!success) {
                Message.printMessage("Не могу подключитсья к FTP серверу!", "Ошибка");
            } else {
                ftpClient.enterLocalPassiveMode();
                ftpClient.changeWorkingDirectory("for_apteka");
                File outFile = new File(patchFile);
                if (outFile.exists()) {
                    ftpClient.deleteFile(fileAutoExchange);
                    InputStream in = new FileInputStream(outFile);
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                    if (!ftpClient.storeFile(fileAutoExchange, in)) {
                        Message.printMessage("Ошибка передачи файла обмена.", "Ошибка");
                    }
                    ftpClient.changeToParentDirectory();
                    if (!ftpClient.changeWorkingDirectory("obmen")) {
                        Message.printMessage("Ошибка смены дирриктории на obmen", "Ошибка");
                    }
                    if (!ftpClient.appendFile(kod, in)) {
                        Message.printMessage("Ошибка передачи файла кода аптеки.", "Ошибка");
                    }
                    ftpClient.logout();
                    ftpClient.disconnect();
                    in.close();
                    if (outFile.exists()) {
                        if (!outFile.delete()) {
                            Message.printMessage("Ошибка удаления файл из: " + patchFile, "Ошибка");
                        }
                    }
                } else {
                    Message.printMessage("Файл не найден: " + patchFile, "Ошибка");
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            }
        } catch (IOException ex) {
            Message.printMessage("Ошибка ФТП сервера!", "Ошибка");
        }
    }

}
