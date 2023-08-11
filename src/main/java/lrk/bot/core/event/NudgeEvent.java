package lrk.bot.core.event;

import com.google.gson.JsonObject;
import lrk.bot.core.BotCore;

public class NudgeEvent extends OtherEvent {
    long fromId; //戳一戳的发起者
    long target; //戳一戳的目标
    Kind kind; //消息来源
    long id; //消息来源ID(群号, QQ号)

    public NudgeEvent(BotCore botCore, JsonObject object) {
        super(botCore, object);
        fromId = object.get("fromId").getAsLong();
        target = object.get("target").getAsLong();
        kind = Kind.valueOf(object.get("subject").getAsJsonObject().get("kind").getAsString());
        id = object.get("subject").getAsJsonObject().get("id").getAsLong();
    }

    public Kind getKind() {
        return kind;
    }

    public long getId() {
        return id;
    }

    public long getFromId() {
        return fromId;
    }

    public long getTarget() {
        return target;
    }

    public enum Kind {
        Group,
        Friend
    }

    @Override
    public String toString() {
        return "NudgeEvent{" +
                "fromId=" + fromId +
                ", target=" + target +
                ", kind=" + kind +
                ", id=" + id +
                '}';
    }
}
