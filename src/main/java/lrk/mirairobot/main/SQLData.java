package lrk.mirairobot.main;

import java.io.*;
import java.sql.*;
import org.sqlite.JDBC;
import java.util.ArrayList;
import java.util.Date;
public class SQLData
{
    private Statement sqldata;
    private Connection sql;
    public SQLData() throws Exception{
        
        Class.forName("org.sqlite.JDBC");
        sql = DriverManager.getConnection("jdbc:sqlite:"+DataBridge.HOME.getPath()+"/Data.db");
        File database = new File(DataBridge.HOME.getPath()+"/Data.db");
        if(database.length()==0){
            sqldata = sql.createStatement();
            sqldata.execute("CREATE TABLE UserReg(ID LONG PRIMARY KEY NOT NULL,REG_TIME DATE NOT NULL,ABLED TEXT NOT NULL,PermissionGroup TEXT NOT NULL);");
            sqldata.execute("CREATE TABLE UserData(ID LONG PRIMARY KEY NOT NULL);");
            System.out.println("Preparing SQL");
        }else{
            sqldata = sql.createStatement();
        }
    }
    //更新用户数据
    public void UpdateData(long qq,String TableName,String TargetKey,String TargetValue) throws SQLException{
        sqldata.executeUpdate("UPDATE "+TableName+" SET "+TargetKey+"= '"+TargetValue+"' WHERE ID="+qq+";");
    }
    //获取用户数据
    public String GetData(long qq,String TableName,String TargetKey) throws SQLException{
        String data = null;
        ResultSet result = sqldata.executeQuery("SELECT "+TargetKey+" FROM "+TableName+" WHERE ID="+qq+";");
        while(result.next()){
            data=result.getString(TargetKey);
        }
        return data;
    }
    public void dispose(){
        try {
            sqldata.close();
            sql.close();
            }
        catch(Exception e){}
    }
}
