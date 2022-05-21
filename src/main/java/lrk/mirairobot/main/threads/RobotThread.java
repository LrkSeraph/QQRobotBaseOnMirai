package lrk.mirairobot.main.threads;

import lrk.mirairobot.main.RobotNotification;
import lrk.mirairobot.main.SQLData;

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
