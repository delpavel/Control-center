package autoExchange.view;

import autoExchange.model.radmin.Radmin;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;


public class RadminController {

    private String ip;
    private Radmin radmin = new Radmin();

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean setComputers() {

        if (radmin.getComputersName(ip) != null) {
            for (Object comp : radmin.getComputersName(ip)) {
                computer.getItems().add(comp);
            }
            return true;
        }
        return false;
    }

    @FXML
    private ListView computer;


    @FXML
    private void control() {


        radmin.setIp(ip);
        radmin.run(computer.getSelectionModel().getSelectedItem().toString());
    }

}
