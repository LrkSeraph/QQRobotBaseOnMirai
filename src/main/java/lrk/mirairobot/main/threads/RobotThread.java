package lrk.mirairobot.main.threads;

import lrk.mirairobot.main.DataBridge;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import lrk.mirairobot.main.SQLData;
import java.sql.SQLException;

public class RobotThread extends Thread{
    
	protected File wordlib_at;
	protected File wordlib_normal;
	protected Properties word_at;
	protected Properties word_normal;
	protected File[] piclist;
	protected File[] pic_word;
	protected File[] voice_list;
    protected SQLData sqldata;
    
	protected void onload(){
		File HOME = DataBridge.getHOME();
		piclist = new File(HOME.getPath()+"/pic").listFiles();
		pic_word = new File(HOME.getPath()+"/words/pictures").listFiles();
		voice_list = new File(HOME.getPath()+"/voice").listFiles();
		try{
			wordlib_at = new File(HOME.getPath()+"/words/word_at.prop");
			wordlib_normal = new File(HOME.getPath()+"/words/word_normal.prop");
			word_at = new Properties();
			word_normal = new Properties();
			word_at.load(new FileInputStream(wordlib_at));
			word_normal.load(new FileInputStream(wordlib_normal));
            //SQL部分
            sqldata = new SQLData();
		}catch(Exception e1){
			System.err.println(e1.getMessage());
		}
	}
	protected void reload() throws IOException{
		File HOME = DataBridge.getHOME();
		piclist = new File(HOME.getPath()+"/pic").listFiles();
		pic_word = new File(HOME.getPath()+"/words/pictures").listFiles();
		voice_list = new File(HOME.getPath()+"/voice").listFiles();
		word_at.load(new FileInputStream(wordlib_at));
		word_normal.load(new FileInputStream(wordlib_normal));
	}
	
}
