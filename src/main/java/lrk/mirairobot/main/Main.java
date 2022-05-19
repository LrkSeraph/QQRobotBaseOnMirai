package lrk.mirairobot.main;

import lrk.mirairobot.core.*;

public class Main{
	public static DataBridge Data;
	public static void main(String args[]) throws Exception{
		Data = DataBridge.getInstance();
		BotCore core = new BotCore(DataBridge.getRobotProp("Port").split(":")[0],Integer.parseInt(DataBridge.getRobotProp("Port").split(":")[1]),"ABCDEFGHIJK",Long.parseLong(DataBridge.getRobotProp("QQ")));
		if(core.addListener(new RobotMain())){
			RobotNotification.Config("添加监听器:"+RobotMain.Name);
		}
	}
}