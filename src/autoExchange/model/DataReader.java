package autoExchange.model;

import autoExchange.view.Message;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;


public class DataReader {

    private Map<String, String> incorrectAutoExchangeFileName = new HashMap<>();
    private File file;
    private Path path;
    private BasicFileAttributes attr;


    public DataReader() {

        incorrectAutoExchangeFileName.put("3", "П07");
        incorrectAutoExchangeFileName.put("65", "П67");
        incorrectAutoExchangeFileName.put("43", "П49");
        incorrectAutoExchangeFileName.put("13", "П15");
        incorrectAutoExchangeFileName.put("7", "П09");
        incorrectAutoExchangeFileName.put("141", "П46");
        incorrectAutoExchangeFileName.put("142", "П48");
        incorrectAutoExchangeFileName.put("46", "П52");
        incorrectAutoExchangeFileName.put("14", "П13");
        incorrectAutoExchangeFileName.put("15", "П14");
        incorrectAutoExchangeFileName.put("23", "П21");
        incorrectAutoExchangeFileName.put("33", "П23");
        incorrectAutoExchangeFileName.put("48", "П50");
        incorrectAutoExchangeFileName.put("45", "П51");
        incorrectAutoExchangeFileName.put("36", "П33");
        incorrectAutoExchangeFileName.put("2", "П06");
    }


    public Map<String, String> getIncorrectAutoExchangeFileName() {
        return incorrectAutoExchangeFileName;
    }


    public String getIp(String kod) throws IOException {

        BufferedReader bufferedReader;
        String ip = "";
        bufferedReader = new BufferedReader(new FileReader("ip.txt"));
        String input;
        while ((input = bufferedReader.readLine()) != null) {
            if (input.substring(0, input.indexOf("\t")).equals(kod)) {
                ip = input.substring(input.indexOf("\t") + 1).trim();
                break;
            }
        }


        return ip;
    }

    public String getZipKod(String kod) {

        if (incorrectAutoExchangeFileName.containsKey(kod)) {
            return "P" + incorrectAutoExchangeFileName.get(kod).substring(1) + "9";
        } else if (Integer.parseInt(kod) < 100) {
            return "P" + kod + "9";
        } else {
            return kod + "1";
        }

    }

    public Long getTimeZipData(String kod) {

        file = new File("\\\\ts1\\Interfarmax\\BaseRozn\\PC\\" + getZipKod(kod) + ".zip");
        path = file.toPath();
        try {
            attr = Files.readAttributes(path, BasicFileAttributes.class);
        } catch (IOException e) {
            return 0L;
        }

        return attr.lastModifiedTime().toMillis();
    }


    public Long getTimeTxtData(String kod) {

        file = new File("\\\\ts1\\Interfarmax\\BaseRozn\\ftpdemo\\" + kod + ".txt");
        path = file.toPath();
        try {
            attr = Files.readAttributes(path, BasicFileAttributes.class);
        } catch (IOException e) {
            return 0L;
        }

        return attr.lastModifiedTime().toMillis();
    }


    public boolean checkInternet(String kod) {

        file = new File("\\\\ts1\\Interfarmax\\BaseRozn\\ftpdemo\\" + kod + ".txt");
        path = file.toPath();
        try {
            attr = Files.readAttributes(path, BasicFileAttributes.class);
        } catch (IOException e) {
            Message.printMessage("Не могу прочитать аттрибуты файла " + file, "Ошибка");
            return false;
        }
        return (System.currentTimeMillis() - attr.lastModifiedTime().toMillis() < 2400000);

    }

}
