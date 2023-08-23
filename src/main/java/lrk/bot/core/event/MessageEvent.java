package lrk.bot.core.event;

import lrk.bot.core.BotCore;
import lrk.bot.core.data.*;
import lrk.bot.core.data.messagetype.*;
import com.google.gson.JsonObject;
import lrk.bot.main.RobotNotification;

import java.io.IOException;

public abstract class MessageEvent extends Event {
    // 发送者ID
    protected long senderID;
    // 消息内容
    protected String message;
    // 消息类型
    protected MessageContentType messageContentType;

    public MessageEvent(BotCore botCore, JsonObject params) {
        super(botCore, params);
    }

    public abstract JsonObject reply(String message) throws IOException;
    public abstract JsonObject reply(Message... message) throws IOException;
    public JsonObject replyIgnoreException(String message) {
        try {
            return reply(message);
        } catch (IOException e) {
            RobotNotification.Warning(e.getMessage());
            return null;
        }
    }
    public JsonObject replyIgnoreException(Message... message) {
        try {
            return reply(message);
        } catch (IOException e) {
            RobotNotification.Warning(e.getMessage());
            return null;
        }
    }

    public long getSender() {
        return senderID;
    }

    public String getMessage() {
        return message;
    }
    
    @Override
    public String toString() {
        return "MessageEvent{" +
                "senderID=" + senderID +
                ", message='" + message + '\'' +
                '}';
    }
}
