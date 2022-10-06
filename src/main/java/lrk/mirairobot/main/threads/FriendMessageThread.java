package lrk.mirairobot.main.threads;

import lrk.mirairobot.core.data.messagetype.Image;
import lrk.mirairobot.core.event.FriendMessageEvent;
import lrk.mirairobot.main.DataBridge;
import lrk.mirairobot.main.RobotNotification;
import lrk.tools.miraiutils.Diu;
import lrk.tools.miraiutils.Pa;

import java.io.IOException;

public class FriendMessageThread extends RobotThread{
	FriendMessageEvent event;
	public FriendMessageThread(FriendMessageEvent event){
		this.event = event;
		onStart();
	}
	@Override
	public void run(){
		try{
			main();
		}catch(IOException e1){
			RobotNotification.Warning(this.getClass().getName() + ":" + e1.getMessage());
		}
	}
	private void main() throws IOException{
		String message = event.getMessage();
		if (message.startsWith("/RandomPic")) {
			event.reply(new Image(null, "https://iw233.cn/API/Random.php", null, null));
		}
		if (message.startsWith("/爬")) {
			event.reply(new Image(null, null, null, DataBridge.getImage(new Pa(Long.parseLong(message.split("@")[1])))));
		}
		if (message.startsWith("/丢")) {
			event.reply(new Image(null, null, null, DataBridge.getImage(new Diu(Long.parseLong(message.split("@")[1])))));
		}
	}
}