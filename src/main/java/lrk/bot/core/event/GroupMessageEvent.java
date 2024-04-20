package lrk.bot.core.event;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lrk.bot.core.BotCore;
import lrk.bot.core.data.MessageContentType;
import lrk.bot.core.data.messagetype.*;
import lrk.bot.utils.MessageUtils;

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
    private final ArrayList<Image> images = new ArrayList<>();
    //消息中的闪照
    private final ArrayList<FlashImage> flashImages = new ArrayList<>();
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
        messageChain.forEach(value -> {
            JsonObject messageData = value.getAsJsonObject();
            MessageContentType messageContentType = MessageContentType.valueOf(messageData.get("type").getAsString());
            switch (messageContentType) {
                case Source -> {
                    messageId = messageData.get("id").getAsLong();
                    messageTime = messageData.get("time").getAsLong();
                }
                case Plain -> message.append(messageData.get("text").getAsString());
                case At -> atUsers.add(messageData.get("target").getAsLong());
                case Image ->
                        images.add(new Image(messageData.has("imageId") ? messageData.get("imageId").getAsString() : null, messageData.has("url") ? messageData.get("url").getAsString() : null, null, null));
                case FlashImage ->
                        flashImages.add(new FlashImage(messageData.has("imageId") ? messageData.get("imageId").getAsString() : null, messageData.has("url") ? messageData.get("url").getAsString() : null, null, null));
                case App -> app = new App(messageData.get("content").getAsString());
            }
        });
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

    public long getSenderID() {
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

    public ArrayList<Image> getImages() {
        return images;
    }

    public ArrayList<FlashImage> getFlashImages() {
        return flashImages;
    }

    public App getApp() {
        return app;
    }

    @Override
    public String toString() {
        return "GroupMessageEvent{groupID=%d, groupNickName='%s', senderID=%d, senderNickName='%s', atUsers=%s, pictures=%s, message=%s, messageId=%d, messageTime=%d, app=%s}".formatted(groupID, groupNickName, senderID, senderNickName, atUsers, images, message, messageId, messageTime, app);
    }
}