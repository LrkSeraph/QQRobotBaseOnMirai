package lrk.mirairobot.core.event;

import lrk.mirairobot.core.BotCore;
import com.google.gson.JsonObject;

public class OtherEvent extends Event {
    public OtherEvent(BotCore botCore, JsonObject object) {
        super(botCore, object);
    }
}