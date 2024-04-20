package lrk.bot.main.threads;

import lrk.bot.core.data.messagetype.Image;
import lrk.bot.core.event.FriendMessageEvent;
import lrk.bot.main.DataBridge;
import lrk.bot.main.RobotNotification;
import lrk.tools.miraiutils.Diu;
import lrk.tools.miraiutils.Pa;

public class FriendMessageThread extends RobotThread{
	FriendMessageEvent event;
	public FriendMessageThread(FriendMessageEvent event){
		this.event = event;
	}
	@Override
	public void run(){
		try{
			core();
		} catch (Exception e) {
			RobotNotification.Warning(String.format("%s->%s: %s", getClass().getName(), e.getClass().getName(), e.getMessage()));
		}
	}

	private void core() throws Exception {
		String message = event.getMessage();
		if (message.startsWith("/爬")) {
			event.reply(new Image(null, null, null, DataBridge.getImage(new Pa(Long.parseLong(message.split("@")[1])))));
		}
		if (message.startsWith("/丢")) {
			event.reply(new Image(null, null, null, DataBridge.getImage(new Diu(Long.parseLong(message.split("@")[1])))));
		}
		event.getFlashImages().forEach(flashImage -> event.replyIgnoreException(flashImage.toImage()));
	}
}