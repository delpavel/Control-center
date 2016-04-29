package autoExchange.view;

import javafx.stage.Stage;
import jfx.messagebox.MessageBox;

public class Message {

    private static Stage primaryStage;

    public static void setPrimaryStage(Stage primaryStage) {
        Message.primaryStage = primaryStage;
    }

    public static void printMessage(String text, String type) {
        MessageBox.show(primaryStage,
                text,
                type,
                MessageBox.ICON_INFORMATION | MessageBox.OK);
    }
}
