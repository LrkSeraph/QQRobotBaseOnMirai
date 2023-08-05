package lrk.bot.core.data.messagetype;

import com.google.gson.*;

//@全体成员

@SuppressWarnings("unused")
public class AtAll extends Message{
	private final String type = "AtAll";

    @Override
	public String toString(){
		JsonObject data = new JsonObject();
		data.addProperty("type",type);
		return data.toString();
	}
	
	public JsonObject toJsonObject(){
		JsonObject data = new JsonObject();
		data.addProperty("type",type);
		return data;
	}
}