package lrk.mirairobot.main;
import java.io.*;
import java.util.*;
import java.sql.*;
import org.sqlite.JDBC;

import lrk.tools.miraiutils.Utils;
import javax.imageio.ImageIO;

public class DataBridge
{
    
	public Scanner Console;
	public String OS;
	public static File HOME;
	public File Robot_Prop;
	private long size;
	private static Properties Robot_Properties;
	private static DataBridge instance = new DataBridge();
	public static File getHOME(){
		return HOME;
	}
	private DataBridge(){
		try {
            init();
        } catch (Exception e) {}
	}
	public static DataBridge getInstance(){
		return instance;
	}
	private void init()throws Exception{
		//获取用户输入
		Console = new Scanner(System.in);
		//判断操作系统，设置相关变量与机器人数据根目录
		OS = System.getProperty("os.name");
		switch(OS){
			case "Linux":{
				if(new File("/storage").exists()){
					OS = "Android";
					HOME = new File("/storage/emulated/0/Robot");
					break;
				}
				HOME = new File(System.getenv("HOME")+"/Robot");
				break;
			}
			case "Windows NT":{
				System.out.print("No.I don't like Windows.");
				System.exit(1);
			}
		}
		if(!HOME.exists()){
			HOME.mkdirs();
			new File(HOME.getPath()+"/tmp").mkdirs();
		}
		//如果机器人配置文件不存在就新建一个
		Robot_Prop = new File(HOME.getPath()+"/robot.properties");
		size = Robot_Prop.length();
        
		Robot_Properties = new Properties();
		try {
			Robot_Properties.load(new FileInputStream(Robot_Prop));
		} catch (IOException e) {}
		//初始化配置
		if(Robot_Properties.get("OS")==null){
			Robot_Properties.put("OS",OS);
		}
		initRobotProp("Port","Set OPQBot's IP address and Port(example:0.0.0.0:8888):","Port not set.");
		initRobotProp("QQ","Set your Robot's QQ number:","Robot's QQ not set.");
		initRobotProp("Mode","Set your Robot's Notification Mode(QUIET,NORMAL,NO_LOG_SAVE):","Wrong Option.");
        //准备sql数据库,在此处预调试
        Class.forName("lrk.mirairobot.main.SQLData");
        SQLData a = new SQLData();
	}
	//初始化获取机器人关键参数
	private void initRobotProp(String value,String RequireMessage,String ErrorMessage){
		if(Robot_Properties.get(value)==null){
			System.out.print(RequireMessage);
			String v = null;
			if((v = Console.next())==null){
				System.out.println(ErrorMessage);
				System.exit(1);
			}
			Robot_Properties.put(value,v);
			refreshprop();
		}
	}
	
	private void refreshprop(){
		try{
			Robot_Properties.store(new FileOutputStream(Robot_Prop,false),"Robot Properties");
			Robot_Properties.clear();
			Robot_Properties.load(new FileInputStream(Robot_Prop));
		}catch(IOException e){}
	}
	//给其他类获取机器人配置信息
	public static boolean setRobotProp(String key,String value){
		Robot_Properties.put(key,value);
		if(Robot_Properties.get(key).equals(value)){
			return true;
		}else{
			return false;
		}
	}
	public static String getRobotProp(String key){
		String value = null;
		if(Robot_Properties.get(key)!=null){
			value = (String)Robot_Properties.get(key);
		}
		return value;
	}
	//文件读取类
	public static byte[] getFileByte(String path){
		File file = null;
		InputStream is = null;
		byte[] file_byte = null;
		try{
			file = new File(path);
			is = new FileInputStream(file);
			file_byte = new byte[(int)file.length()];
			is.read(file_byte);
			is.close();
		}catch(FileNotFoundException e1){
			System.err.println(e1.getMessage());
		}catch(IOException e2){
			System.err.println(e2.getMessage());
		}
		return file_byte;
	}
	public static String getFileBase64(String path){
		return Base64.getEncoder().encodeToString(getFileByte(path));
	}
    public static FileOutputStream openFileOutput(String path,boolean append){
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path, append);
        } catch (FileNotFoundException e) {}
        return out;
    }
	public static String getImage(Utils util) throws IOException{
	    File image = new File(HOME.getPath()+"/tmp/"+System.currentTimeMillis()+".png");
	    ImageIO.write(util.getImage(),"png",openFileOutput(image.getPath(),false));
	    util.dispose();
	    return getFileBase64(image.getPath());
	}
}
