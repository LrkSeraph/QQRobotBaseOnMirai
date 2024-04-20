package lrk.bot.main;

import lrk.bot.core.BotCore;

public class Main{
	public static void main(String[] args) throws Exception{
		BotCore core = BotCore.DEFAULT;
		if(core.addListener(new RobotMain())){
			RobotNotification.Config("添加监听器: %s".formatted(RobotMain.Name));
		}
	}
}