package lrk.mirairobot.core.data.messagetype;

import com.google.gson.JsonObject;

public class Json extends Message {
	private final String type = "Json";
	private String json = "";

	public Json(String json) {
		this.json = json;
	}
	
	public String getJson() {
		return json;
	}

	@Override
	public String toString() {
		JsonObject data = new JsonObject();
		data.addProperty("json",json);
		return data.toString();
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject data = new JsonObject();
		data.addProperty("json",json);
		return data;
	}
	
	
}
