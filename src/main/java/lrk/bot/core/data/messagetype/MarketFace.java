package lrk.bot.core.data.messagetype;

import com.google.gson.JsonObject;

//商城表情

@SuppressWarnings("unused")
public class MarketFace extends Message {

    private final String type = "MarketFace";
    private final int id;//唯一标识
    private final String name;//表情名称

    public MarketFace(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        JsonObject data = new JsonObject();
        data.addProperty("type", type);
        data.addProperty("id", id);
        data.addProperty("name", name);
        return data.toString();
    }

    @Override
    public JsonObject toJsonObject() {
        JsonObject data = new JsonObject();
        data.addProperty("type", type);
        data.addProperty("id", id);
        data.addProperty("name", name);
        return data;
    }
}
