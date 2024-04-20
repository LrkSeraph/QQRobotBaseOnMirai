package lrk.bot.main;

import lrk.bot.core.event.Event;
import lrk.bot.core.event.FriendMessageEvent;
import lrk.bot.core.event.GroupMessageEvent;
import lrk.bot.core.event.NudgeEvent;
import lrk.bot.core.listener.EventHandler;
import lrk.bot.core.listener.Listener;
import lrk.bot.main.threads.FriendMessageThread;
import lrk.bot.main.threads.GroupMessageThread;
import lrk.bot.utils.MessageUtils;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RobotMain implements Listener {
    public static String Name = "RobotMain";
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    public void onEnabled() {
        threadPoolExecutor = new ThreadPoolExecutor(10, 20, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(500));
    }

    @Override
    public void onRemoved() {

    }

    @EventHandler
    public void onEvent(Event event) {
        switch (event) {
            case FriendMessageEvent friendMessageEvent ->
                    RobotNotification.Info("FriendMessage: %s".formatted(friendMessageEvent.getParams()));
            case GroupMessageEvent groupMessageEvent ->
                    RobotNotification.Info("GroupMessage: %s".formatted(groupMessageEvent.getParams()));
            case NudgeEvent nudgeEvent -> RobotNotification.Info("NudgeEvent: %s".formatted(nudgeEvent.getParams()));
            default -> throw new IllegalStateException("Unexpected value: %s".formatted(event));
        }
    }

    @EventHandler
    public void onFriendMessage(FriendMessageEvent event) {
        threadPoolExecutor.submit(new FriendMessageThread(event));
    }

    @EventHandler
    public void onGroupMessage(GroupMessageEvent event) {
        threadPoolExecutor.submit(new GroupMessageThread(event));
    }

    @EventHandler
    public void onNudgeEvent(NudgeEvent event) throws IOException {
        if (event.getKind() == NudgeEvent.Kind.Group && event.getTarget() == event.getBotCore().getQq() && event.getFromId() != event.getBotCore().getQq())
            event.getBotCore().sendGroupMessage(event.getBotCore().nudge(MessageUtils.sendGroupNudge(event.getId(), event.getFromId())));
    }
}
