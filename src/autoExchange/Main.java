package autoExchange;


import autoExchange.view.Controller;
import autoExchange.view.Message;
import autoExchange.view.RadminController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {


    public void showRadmin(String ip) throws IOException {

        Stage radminStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/radmin.fxml"));
        radminStage.setTitle("Управление ТО");
        radminStage.getIcons().add(new Image("resources/images/del.png"));
        radminStage.setScene(new Scene(loader.load()));

        RadminController radminController = loader.getController();
        radminController.setIp(ip);
        if (radminController.setComputers()) {
            radminStage.show();
        }


    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/form.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setMain(this);
        primaryStage.setTitle("Управление автообменом");
        primaryStage.getIcons().add(new Image("resources/images/del.png"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        Message.setPrimaryStage(primaryStage);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
