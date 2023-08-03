package lrk.bot.core.event;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lrk.bot.core.BotCore;
import lrk.bot.core.data.MessageContentType;
import lrk.bot.core.data.messagetype.FlashImage;
import lrk.bot.core.data.messagetype.Image;
import lrk.bot.core.data.messagetype.Message;
import lrk.bot.core.data.messagetype.Plain;
import lrk.bot.utils.MessageUtils;

import java.io.IOException;
import java.util.ArrayList;

public class FriendMessageEvent extends MessageEvent {
    //消息中的文本
    private final StringBuilder message = new StringBuilder();
    //消息中的图片或大表情
    private final ArrayList<Image> images = new ArrayList<>();
    //消息中的闪照
    private final ArrayList<FlashImage> flashImages = new ArrayList<>();
    // 消息id
    private long messageID;


    public FriendMessageEvent(BotCore botCore, JsonObject data) {
        super(botCore, data);
        senderID = data.getAsJsonObject("sender").get("id").getAsLong();
        JsonArray messageChain = data.getAsJsonArray("messageChain");
        messageChain.forEach(value -> {
            JsonObject messageData = value.getAsJsonObject();
            MessageContentType messageContentType = MessageContentType.valueOf(messageData.get("type").getAsString());
            switch (messageContentType) {
                case Source -> messageID = messageData.get("id").getAsLong();
                case Plain -> message.append(messageData.get("text").getAsString());
                case Image ->
                        images.add(new Image(messageData.has("imageId") ? messageData.get("imageId").getAsString() : null, messageData.has("url") ? messageData.get("url").getAsString() : null, null, null));
                case FlashImage ->
                        flashImages.add(new FlashImage(messageData.has("imageId") ? messageData.get("imageId").getAsString() : null, messageData.has("url") ? messageData.get("url").getAsString() : null, null, null));
            }
        });
    }

    @Override
    public JsonObject reply(String message) throws IOException {
        return getBotCore().sendFriendMessage(
                MessageUtils.sendMessageToFriend(senderID, new Plain(message))
        );
    }

    @Override
    public JsonObject reply(Message... message) throws IOException {
        return getBotCore().sendFriendMessage(MessageUtils.sendMessageToFriend(senderID, message));
    }

    public String getMessage() {
        return message.toString();
    }

    public long getMessageID() {
        return messageID;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public ArrayList<FlashImage> getFlashImages() {
        return flashImages;
    }

    @Override
    public String toString() {
        return "FriendMessageEvent{" +
                "message=" + message +
                ", images=" + images +
                ", flashImages=" + flashImages +
                ", messageID=" + messageID +
                '}';
    }
}