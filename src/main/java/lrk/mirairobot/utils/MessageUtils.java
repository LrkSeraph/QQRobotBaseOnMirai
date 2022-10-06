package lrk.mirairobot.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lrk.mirairobot.core.data.messagetype.Message;

@SuppressWarnings("unused")
public class MessageUtils {

    public static JsonObject sendMessageToFriend(long qq, Message... messages) {
        JsonObject object = new JsonObject();
        object.addProperty("target", qq);
        JsonArray messageChain = new JsonArray();
        for (Message message : messages) {
            messageChain.add(message.toJsonObject());
        }
        object.add("messageChain", messageChain);
        return object;
    }

    public static JsonObject sendMessageToGroup(long groupID, Message... messages) {
        JsonObject object = new JsonObject();
        object.addProperty("target", groupID);
        JsonArray messageChain = new JsonArray();
        for (Message message : messages) {
            messageChain.add(message.toJsonObject());
        }
        object.add("messageChain", messageChain);
        return object;
    }

    /*
     *戳一戳
     *@param target 目标QQ号
     */
    public static JsonObject sendGroupNudge(long groupID, long target) {
        JsonObject data = new JsonObject();
        data.addProperty("target", target);
        data.addProperty("subject", groupID);
        data.addProperty("kind", "Group");
        return data;
    }

    /*
     * 撤回消息
     * @param targetGroupOrFriend 目标群或QQ号
     * @param messageId 要撤回的消息ID
     */
    public static JsonObject recall(long targetGroupOrFriend, long messageId) {
        JsonObject data = new JsonObject();
        data.addProperty("messageId", messageId);
        data.addProperty("target", targetGroupOrFriend);
        return data;
    }

    /*
     * 禁言群成员
     * @param target 目标群
     * @param memberId 目标群成员
     * @param messageId 要撤回的消息ID
     */
    public static JsonObject mute(long groupId, long memberId, long time) {
        JsonObject data = new JsonObject();
        data.addProperty("target", groupId);
        data.addProperty("memberId", memberId);
        data.addProperty("time", time);
        return data;
    }
}