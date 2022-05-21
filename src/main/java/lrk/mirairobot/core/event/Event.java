package lrk.mirairobot.core.event;

import lrk.mirairobot.core.BotCore;
import com.google.gson.JsonObject;

public abstract class Event {
    private BotCore botCore;
    private JsonObject params;
    private boolean cancelled;

    public Event(BotCore botCore, JsonObject params) {
        this.botCore = botCore;
        this.params = params;
        cancelled = false;
    }

    public BotCore getBotCore() {
        return botCore;
    }

    public JsonObject getParams() {
        return params;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
