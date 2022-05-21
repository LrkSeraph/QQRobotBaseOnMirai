package lrk.mirairobot.main.threads;

import lrk.mirairobot.core.data.messagetype.Image;
import lrk.mirairobot.core.event.GroupMessageEvent;
import lrk.mirairobot.main.DataBridge;
import lrk.mirairobot.main.RobotNotification;
import lrk.tools.miraiutils.丢;
import lrk.tools.miraiutils.爬;
import lrk.tools.miraiutils.运势;

public class GroupMessageThread extends RobotThread {
    GroupMessageEvent event;

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
        String message = event.getMessage();
        if (message.startsWith("/丢")) {
            if (event.getAtUsers().size() != 0) {
                for (Long aLong : event.getAtUsers()) {
                    event.reply(new Image(null, null, null, DataBridge.getImage(new 丢(aLong))));
                }
            }
        }
        if (message.startsWith("/爬")) {
            if (event.getAtUsers().size() != 0) {
                for (Long aLong : event.getAtUsers()) {
                    event.reply(new Image(null, null, null, DataBridge.getImage(new 爬(aLong))));
                }
            }
        }
        if (message.startsWith("/运势")) {
            event.reply(new Image(null, null, null, DataBridge.getImage(new 运势())));
        }
    }
}
