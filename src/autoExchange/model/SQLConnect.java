package autoExchange.model;


import java.sql.*;

import autoExchange.view.Message;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

public class SQLConnect {

    private Connection con;
    private Statement st;
    private ResultSet rs;

    private Connection getConnection() {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setApplicationName("jdbc:sqlserver://");
        ds.setServerName("srv-sql-03");
        ds.setDatabaseName("baserozn");
        ds.setPortNumber(1433);
        ds.setUser("sa");
        ds.setPassword("sa1c");

        try {
            return ds.getConnection();
        } catch (SQLServerException e) {
            Message.printMessage("Ошибка подключения к серверу БД!", "Ошибка");
        }
        return null;
    }

    public ResultSet sqlConnect() {

        con = getConnection();

        if (con != null) {
            try {
                st = con.createStatement();
                //Statement позволяет отправлять запросы базе данных
                rs = st.executeQuery("select CODE, SP11102, SP10296  from SC10237 where (ISMARK=0) AND (CODE<1000)");
                return rs;
            } catch (SQLException e) {
                Message.printMessage("Ошибка запроса к БД!", "Ошибка");
            }
        }
        return null;
    }

    public void connectClose() {

        try {
            rs.close();
        } catch (SQLException e) {
            Message.printMessage("Ошибка закрытия ResultSet!", "Ошибка");
        }
        try {
            st.close();
        } catch (SQLException e) {
            Message.printMessage("Ошибка закрытия Statement!", "Ошибка");
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                Message.printMessage("Ошибка закрытия Connection!", "Ошибка");
            }
        }
    }


}
