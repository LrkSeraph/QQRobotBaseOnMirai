package lrk.bot.main.threads;

import lrk.bot.core.data.messagetype.Image;
import lrk.bot.core.event.GroupMessageEvent;
import lrk.bot.main.DataBridge;
import lrk.bot.main.RobotNotification;
import lrk.bot.utils.MessageUtils;
import lrk.tools.miraiutils.Diu;
import lrk.tools.miraiutils.Pa;

import java.util.ArrayList;

public class GroupMessageThread extends RobotThread {
    GroupMessageEvent event;
    String[] blackListWord = {"。。。"};
    ArrayList<String> blackListImage = new ArrayList<>() {{
        add("{BD0411CA-79AD-8DD7-834B-79E10E9A7F33}.jpg");
    }};

    public GroupMessageThread(GroupMessageEvent event) {
        this.event = event;
        onStart();
    }

    @Override
    public void run() {
        try {
            core();
        } catch (Exception e1) {
            RobotNotification.Warning(String.format("%s: %s", getClass().getName(), e1.getMessage()));
        }
    }

    private void core() throws Exception {
        event.getBotCore().nudge(MessageUtils.sendGroupNudge(event.getGroupID(),event.getSender()));
        if (event.getAtUsers().contains(Long.parseLong(DataBridge.getRobotProp("QQ")))) {
            String message = event.getMessage().stripLeading();
        }
        if (!event.getImages().isEmpty()) {
            for (Image img : event.getImages()) {
                if (blackListImage.contains(img.getImageId())) {
                    event.getBotCore().recall(MessageUtils.recall(event.getGroupID(), event.getMessageId()));
                    event.getBotCore().mute(MessageUtils.mute(event.getGroupID(), event.getSender(), 60));
                }
            }
        }
        String message = event.getMessage();
        for (String s : blackListWord) {
            if (message.strip().contains(s)) {
                event.getBotCore().recall(MessageUtils.recall(event.getGroupID(), event.getMessageId()));
                event.getBotCore().mute(MessageUtils.mute(event.getGroupID(), event.getSender(), 60));
            }
        }
        if (message.startsWith("/RandomPic")) {
            event.reply(new Image(null, "https://iw233.cn/API/Random.php", null, null));
        }
        if (message.startsWith("/丢")) {
            if (!event.getAtUsers().isEmpty()) {
                for (Long aLong : event.getAtUsers()) {
                    event.reply(new Image(null, null, null, DataBridge.getImage(new Diu(aLong))));
                }
            }
        }
        if (message.startsWith("/爬")) {
            if (!event.getAtUsers().isEmpty()) {
                for (Long aLong : event.getAtUsers()) {
                    event.reply(new Image(null, null, null, DataBridge.getImage(new Pa(aLong))));
                }
            }
        }
    }
}
