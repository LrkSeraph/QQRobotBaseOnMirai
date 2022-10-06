package lrk.mirairobot.main.threads;

import lrk.mirairobot.core.data.messagetype.Image;
import lrk.mirairobot.core.event.GroupMessageEvent;
import lrk.mirairobot.main.DataBridge;
import lrk.mirairobot.main.RobotNotification;
import lrk.mirairobot.utils.MessageUtils;
import lrk.tools.miraiutils.Diu;
import lrk.tools.miraiutils.Pa;

import java.util.ArrayList;

public class GroupMessageThread extends RobotThread {
    GroupMessageEvent event;
    String[] blackListWord = {"哼哼", "哼啊", "114", "514", "1919180", "（悲）", "(悲)", "（喜）", "(喜)", "一个一个", "甚至九", "会员制", "撅"};
    ArrayList<String> blackListImage = new ArrayList<>() {{
        add("{DAD31E4F-7B76-2422-4FC9-BA0F4DFEE7B9}.jpg");
        add("{7816F7A0-5515-712F-AEE1-DA946789C0A4}.gif");
    }};

    public GroupMessageThread(GroupMessageEvent event) {
        this.event = event;
        onStart();
    }

    @Override
    public void run() {
        try {
            main();
        } catch (Exception e1) {
            RobotNotification.Warning(this.getClass().getName() + ":" + e1.getMessage());
        }
    }

    private void main() throws Exception {
        //event.getBotCore().nudge(MessageUtils.sendGroupNudge(event.getGroupID(),event.getSender()));
        if (event.getAtUsers().contains(Long.parseLong(DataBridge.getRobotProp("QQ")))) {
            String message = event.getMessage().stripLeading();
        }
        if (event.getPictures().size() != 0) {
            for (Image img : event.getPictures()) {
                if (blackListImage.contains(img.getImageId())) {
                    event.getBotCore().recall(MessageUtils.recall(event.getGroupID(), event.getMessageId()));
                    event.getBotCore().mute(MessageUtils.mute(event.getGroupID(), event.getSender(), 60));
                }
            }
        }
        String message = event.getMessage();
        for (String s : blackListWord) {
            if (message.contains(s)) {
                event.getBotCore().recall(MessageUtils.recall(event.getGroupID(), event.getMessageId()));
                event.getBotCore().mute(MessageUtils.mute(event.getGroupID(), event.getSender(), 60));
            }
        }
        if (message.startsWith("/RandomPic")) {
            event.reply(new Image(null, "https://iw233.cn/API/Random.php", null, null));
        }
        if (message.startsWith("/丢")) {
            if (event.getAtUsers().size() != 0) {
                for (Long aLong : event.getAtUsers()) {
                    event.reply(new Image(null, null, null, DataBridge.getImage(new Diu(aLong))));
                }
            }
        }
        if (message.startsWith("/爬")) {
            if (event.getAtUsers().size() != 0) {
                for (Long aLong : event.getAtUsers()) {
                    event.reply(new Image(null, null, null, DataBridge.getImage(new Pa(aLong))));
                }
            }
        }
    }
}
