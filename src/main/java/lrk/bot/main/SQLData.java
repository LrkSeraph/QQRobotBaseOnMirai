package lrk.bot.main;

import java.io.File;
import java.sql.*;

public class SQLData {
    private final static SQLData INSTANCE;

    static {
        try {
            INSTANCE = new SQLData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final Statement sqlData;
    private final Connection sql;

    private SQLData() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        sql = DriverManager.getConnection("jdbc:sqlite:" + DataBridge.HOME.getPath() + "/RobotData.db");
        File database = new File(DataBridge.HOME.getPath() + "/RobotData.db");
        if (database.length() == 0) {
            sqlData = sql.createStatement();
            sqlData.execute("CREATE TABLE UserReg(ID LONG PRIMARY KEY NOT NULL,REG_TIME DATE NOT NULL,ABLE TEXT NOT NULL,PermissionGroup TEXT NOT NULL);");
            sqlData.execute("CREATE TABLE UserData(ID LONG PRIMARY KEY NOT NULL);");
            System.out.println("Preparing SQL");
        } else {
            sqlData = sql.createStatement();
        }
    }

    public static SQLData getInstance() {
        return INSTANCE;
    }

    //更新用户数据
    public void UpdateData(long qq, String TableName, String TargetKey, String TargetValue) throws SQLException {
        sqlData.executeUpdate("UPDATE " + TableName + " SET " + TargetKey + "= '" + TargetValue + "' WHERE ID=" + qq + ";");
    }

    //获取用户数据
    public String GetData(long qq, String TableName, String TargetKey) throws SQLException {
        String data = null;
        ResultSet result = sqlData.executeQuery("SELECT " + TargetKey + " FROM " + TableName + " WHERE ID=" + qq + ";");
        while (result.next()) {
            data = result.getString(TargetKey);
        }
        return data;
    }

    public void dispose() {
        try {
            sqlData.close();
            sql.close();
        } catch (Exception ignored) {
        }
    }
}
