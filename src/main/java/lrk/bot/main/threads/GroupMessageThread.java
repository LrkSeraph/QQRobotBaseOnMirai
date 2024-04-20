package lrk.bot.main.threads;

import com.google.gson.JsonArray;
import lrk.bot.core.data.messagetype.Image;
import lrk.bot.core.event.GroupMessageEvent;
import lrk.bot.main.AI;
import lrk.bot.main.DataBridge;
import lrk.bot.main.RobotNotification;
import lrk.bot.utils.MessageUtils;
import lrk.tools.miraiutils.Diu;
import lrk.tools.miraiutils.Pa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class GroupMessageThread extends RobotThread {
    GroupMessageEvent event;
    String[] blackListWord = {"。。。", "6"};
    ArrayList<String> blackListImage = new ArrayList<>() {{
        add("{BD0411CA-79AD-8DD7-834B-79E10E9A7F33}.jpg");
    }};

    public GroupMessageThread(GroupMessageEvent event) {
        this.event = event;
    }

    @Override
    public void run() {
        try {
            core();
        } catch (Exception e) {
            RobotNotification.Warning(String.format("%s->%s: %s", getClass().getName(), e.getClass().getName(), e.getMessage()));
        }
    }

    private void core() throws Exception {
        event.getBotCore().nudge(MessageUtils.sendGroupNudge(event.getGroupID(), event.getSender()));
        AI:
        {
            if (event.getAtUsers().contains(Long.parseLong(DataBridge.getRobotProp("QQ")))) {
                String message = event.getMessage().stripLeading();
                if (message.equals("/cleanHistory")) {
                    AI.cleanHistory(event.getSenderID());
                    event.reply("Done.");
                    System.out.println(AI.all_history.get(event.getSenderID()));
                    break AI;
                }
                AI ai = new AI(UUID.randomUUID().toString().substring(0, 10));
                JsonArray context = AI.all_history.get(event.getSenderID());
                ai.askSync(context == null ? new JsonArray() : context, message, (history, answer) -> {
                    try {
                        event.reply(answer);
                    } catch (IOException e) {
                        RobotNotification.Warning(String.format("%s->%s: %s", getClass().getName(), e.getClass().getName(), e.getMessage()));
                    }
                    while (history.toString().length() > 5000) history.remove(0);
                    AI.all_history.put(event.getSenderID(), history);
                });
            }
        }
        // Image blacklist
        if (!event.getImages().isEmpty()) {
            for (Image img : event.getImages()) {
                if (blackListImage.contains(img.getImageId())) {
                    event.getBotCore().recall(MessageUtils.recall(event.getGroupID(), event.getMessageId()));
                    event.getBotCore().mute(MessageUtils.mute(event.getGroupID(), event.getSender(), 60));
                }
            }
        }
        // Word blacklist
        String message = event.getMessage();
        for (String s : blackListWord) {
            if (message.strip().contains(s)) {
                event.getBotCore().recall(MessageUtils.recall(event.getGroupID(), event.getMessageId()));
                event.getBotCore().mute(MessageUtils.mute(event.getGroupID(), event.getSender(), 60));
            }
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
