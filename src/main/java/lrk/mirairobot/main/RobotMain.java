package lrk.mirairobot.main;

import lrk.mirairobot.core.event.Event;
import lrk.mirairobot.core.event.FriendMessageEvent;
import lrk.mirairobot.core.event.GroupMessageEvent;
import lrk.mirairobot.core.listener.EventHandler;
import lrk.mirairobot.core.listener.Listener;
import lrk.mirairobot.main.threads.FriendMessageThread;
import lrk.mirairobot.main.threads.GroupMessageThread;
public class RobotMain implements Listener {
    public static String Name = "RobotMain";

    @Override
    public void OnEnabled() {

    }

    @Override
    public void OnRemoved() {

    }

    @EventHandler
    public void onEvent(Event event) {
        RobotNotification.Info("新事件:(" + event.getParams() + ")");
    }

    @EventHandler
    public void onFriendMessage(FriendMessageEvent event) {
        new FriendMessageThread(event).start();
    }
    @EventHandler
    public void onGroupMessage(GroupMessageEvent event) {
    	new GroupMessageThread(event).start();
	}
	
}
