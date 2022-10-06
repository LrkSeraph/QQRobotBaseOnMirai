package lrk.mirairobot.core.event;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lrk.mirairobot.core.BotCore;
import lrk.mirairobot.core.data.MessageType;
import lrk.mirairobot.core.data.messagetype.App;
import lrk.mirairobot.core.data.messagetype.Image;
import lrk.mirairobot.core.data.messagetype.Message;
import lrk.mirairobot.core.data.messagetype.Plain;
import lrk.mirairobot.utils.MessageUtils;

import java.io.IOException;
import java.util.ArrayList;

public class GroupMessageEvent extends MessageEvent {
    // 群号
    private final long groupID;
    // 群名称
    private final String groupNickName;
    // 发送者QQ号
    private final long senderID;
    // 发送者群名片
    private final String senderNickName;
    //@ 到的用户ID
    private final ArrayList<Long> atUsers = new ArrayList<>();
    //消息中的图片
    private final ArrayList<Image> pictures = new ArrayList<>();
    //文字消息
    private final StringBuilder message = new StringBuilder();
    // 消息顺序号(所有消息类型均有)
    private long messageId;
    // 消息发送时间(所有消息类型均有)
    private long messageTime;
    //App消息
    private App app;

    public GroupMessageEvent(BotCore botCore, JsonObject data) {
        super(botCore, data);
        groupID = data.getAsJsonObject("sender").get("group").getAsJsonObject().get("id").getAsLong();
        groupNickName = data.getAsJsonObject("sender").get("group").getAsJsonObject().get("name").getAsString();
        senderID = data.getAsJsonObject("sender").get("id").getAsLong();
        senderNickName = data.getAsJsonObject("sender").get("memberName").getAsString();
        JsonArray messageChain = data.getAsJsonArray("messageChain");
        for (int i = 0; i < messageChain.size(); i++) {
            JsonObject messageData = messageChain.get(i).getAsJsonObject();
            MessageType messageType = MessageType.valueOf(messageData.get("type").getAsString());
            switch (messageType) {
                case Source: {
                    messageId = messageData.get("id").getAsLong();
                    messageTime = messageData.get("time").getAsLong();
                    break;
                }
                case Plain: {
                    message.append(messageData.get("text").getAsString());
                    break;
                }
                case At: {
                    atUsers.add(messageData.get("target").getAsLong());
                    break;
                }
                case Image: {
                    System.out.println(messageData.has("imageId") ? messageData.get("imageId").getAsString() : null);
                    System.out.println(messageData.has("url") ? messageData.get("url").getAsString() : null);
                    pictures.add(new Image(messageData.has("imageId") ? messageData.get("imageId").getAsString() : null, messageData.has("url") ? messageData.get("url").getAsString() : null, null, null));
                    break;
                }
                case App: {
                    app = new App(messageData.get("content").getAsString());
                }
            }
        }
    }

    @Override
    public JsonObject reply(String message) throws IOException {
        return getBotCore().sendGroupMessage(
                MessageUtils.sendMessageToGroup(groupID, new Plain(message))
        );
    }

    @Override
    public JsonObject reply(Message... message) throws IOException {
        return getBotCore().sendGroupMessage(MessageUtils.sendMessageToGroup(groupID, message));
    }

    public long getGroupID() {
        return groupID;
    }

    public String getGroupNickName() {
        return groupNickName;
    }

    public long getSender() {
        return senderID;
    }

    public String getSenderNickName() {
        return senderNickName;
    }

    public String getMessage() {
        return message.toString();
    }

    public long getMessageId() {
        return messageId;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public ArrayList<Long> getAtUsers() {
        return atUsers;
    }

    public ArrayList<Image> getPictures() {
        return pictures;
    }

    public App getApp() {
        return app;
    }

    @Override
    public String toString() {
        return "";
    }

}