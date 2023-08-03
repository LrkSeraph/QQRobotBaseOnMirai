package lrk.bot.main.threads;

import lrk.bot.main.RobotNotification;
import lrk.bot.main.SQLData;

public class RobotThread extends Thread{
    protected SQLData SqlDatabase;
    
	protected void onStart(){
		try{
			SqlDatabase = SQLData.getInstance();
		}catch(Exception e1){
			RobotNotification.Warning(e1.getMessage());
		}
	}
}
