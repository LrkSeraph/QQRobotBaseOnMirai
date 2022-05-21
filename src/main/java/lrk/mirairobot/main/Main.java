package lrk.mirairobot.main;

import lrk.mirairobot.core.BotCore;

public class Main{
	public static void main(String[] args) throws Exception{
		BotCore core = BotCore.getInstance();
		if(core.addListener(new RobotMain())){
			RobotNotification.Config("添加监听器:"+RobotMain.Name);
		}
	}
}