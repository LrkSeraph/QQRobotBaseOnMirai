package lrk.mirairobot.main.threads;

import lrk.mirairobot.core.event.FriendMessageEvent;
import lrk.mirairobot.main.RobotNotification;

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

	}
}