package autoExchange.view;

import autoExchange.Main;
import autoExchange.model.AutoExchange;
import autoExchange.model.DataReader;
import autoExchange.model.SQLConnect;
import autoExchange.model.radmin.Radmin;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Controller {

    private ResultSet rs;
    private ObservableList<ObservableList> tableData = FXCollections.observableArrayList();
    private SQLConnect sqlConnect = new SQLConnect();
    protected DataReader dataReader = new DataReader();
    private AutoExchange autoExchange = new AutoExchange();
    // protected Radmin radmin = new Radmin();
    private Main main;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


    @FXML
    private TableView table;
    @FXML
    private TableColumn kod;
    @FXML
    private TableColumn firm;
    @FXML
    private TableColumn address;
    @FXML
    private TableColumn internet;
    @FXML
    private TableColumn exchange;


    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    private void initialize() {

        autoExchange.setDataReader(dataReader);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+03:00"));

        rs = sqlConnect.sqlConnect();
        if (rs != null) {
            table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            kod.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, Integer>, ObservableValue<Integer>>() {
                public ObservableValue<Integer> call(TableColumn.CellDataFeatures<ObservableList, Integer> param) {
                    return new SimpleObjectProperty(param.getValue().get(0));
                }
            });

            firm.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(1).toString());
                }
            });

            address.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(2).toString());
                }
            });

            internet.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, Date>, ObservableValue<Date>>() {
                public ObservableValue<Date> call(TableColumn.CellDataFeatures<ObservableList, Date> param) {
                    return new SimpleObjectProperty(param.getValue().get(3));
                }
            });

            exchange.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, Date>, ObservableValue<Date>>() {
                public ObservableValue<Date> call(TableColumn.CellDataFeatures<ObservableList, Date> param) {
                    return new SimpleObjectProperty(param.getValue().get(4).toString());
                }
            });


            try {
                while (rs.next()) {
                    ObservableList<Object> rowData = FXCollections.observableArrayList();
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        if (i == 1) {
                            rowData.add(rs.getInt(i));
                        } else {
                            rowData.add(rs.getString(i).trim());
                        }
                    }
                    rowData.add("");
                    rowData.add("");
                    tableData.add(rowData);

                }
                table.setItems(tableData);
                rs.close();
                sqlConnect.connectClose();
            } catch (SQLException e) {
                Message.printMessage("Ошибка заполнения таблицы данных!", "Ошибка");
            }

        }
    }

    @FXML
    private void radmin() throws IOException {

        String ip;
        String input = table.getSelectionModel().getSelectedItem().toString();
        ip = dataReader.getIp(input.substring(1, input.indexOf(",")));
        if (ip != "") {
            main.showRadmin(ip);
        } else {
            Message.printMessage("Не найден IP по ТО:" + input + "! Обратитесь к разработчику ПО!", "Ошибка");
        }


    }

    @FXML
    private void unloading() {

        ObservableList<String> rowData;

        for (int i = 0; i < tableData.size(); i++) {

            rowData = tableData.get(i);
            rowData.set(4, sdf.format(new Date(dataReader.getTimeZipData(kod.getCellData(i).toString()))));
            tableData.set(i, rowData);
        }
        table.refresh();
    }


    @FXML
    private void internetOnTO() {

        ObservableList<String> rowData;

        for (int i = 0; i < tableData.size(); i++) {

            rowData = tableData.get(i);
            rowData.set(3, sdf.format(new Date(dataReader.getTimeTxtData(kod.getCellData(i).toString()))));
            tableData.set(i, rowData);
        }
        table.refresh();
    }

    @FXML
    private void downloadAutoExchange() {

        //РАЗРАБОТКА КОДА НИЖЕ ОТКЛАДЫВАЕТСЯ
////////////////////////////////////////////////////////////////////
/*
        Radmin radmin = new Radmin();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    radmin.getFileAutoExchangeFromPharmacy("10.13.211.59");
                } catch (IOException e) {
                    Message.printMessage("Не могу подлкючиться к серверу!", "Ошибка");
                }
            }
        }).start();*/

///////////////////////////////////////////////////////////////////
        int count = 0;

        for (int i = 0; i < table.getSelectionModel().getSelectedItems().size(); i++) {
            String str = table.getSelectionModel().getSelectedItems().get(i).toString();
            String kod = str.substring(1, str.indexOf(","));
            autoExchange.writeFileAutoExchange("ReadFrom=", kod);
            autoExchange.runBat("Загрузка", kod);
            count = 1;
        }
        if (count != 0) {
            Message.printMessage("Если не было ошибок, данные загружены в ЦБ. Нужно проверить лог файл в 1С!", "Информация");
        } else {
            Message.printMessage("Не выбран ТО в таблице!", "Информация");
        }
    }

    @FXML
    private void sendAutoExchange() {

        int count = 0;

        for (int i = 0; i < table.getSelectionModel().getSelectedItems().size(); i++) {
            String str = table.getSelectionModel().getSelectedItems().get(i).toString();
            String kod = str.substring(1, str.indexOf(","));
            if (dataReader.checkInternet(kod)) {
                autoExchange.writeFileAutoExchange("WriteTo=", kod);
                autoExchange.runBat("Выгрузка", kod);
                count = 1;
            } else {
                Message.printMessage("Данные с аптеки не поступали более 40 минут, проверьте интернет на ТО, код которого " + kod, "Ошибка");
                return;
            }
        }
        if (count != 0) {
            Message.printMessage("Если не было ошибок, данные выгружены. Сообщите о выполнении автообмена в течении 15 минут!", "Информация");
        } else {
            Message.printMessage("Не выбран ТО в таблице!", "Информация");
        }

    }

}
