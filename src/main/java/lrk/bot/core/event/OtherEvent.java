package lrk.bot.core.event;

import lrk.bot.core.BotCore;
import com.google.gson.JsonObject;

public class OtherEvent extends Event {
    public OtherEvent(BotCore botCore, JsonObject object) {
        super(botCore, object);
    }
}