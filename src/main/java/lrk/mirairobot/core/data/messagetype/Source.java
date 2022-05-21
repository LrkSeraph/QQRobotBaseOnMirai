package lrk.mirairobot.core.data.messagetype;

import com.google.gson.JsonObject;

public class Source extends Message {
	private final String type = "Source";
	private int id;//消息id
	private int time;//消息时间戳

	public Source(int id, int time) {
		this.id = id;
		this.time = time;
	}

	public int getId() {
		return id;
	}

	public int getTime() {
		return time;
	}
	
	@Override
	public String toString() {
		JsonObject data = new JsonObject();
		data.addProperty("type",type);
		data.addProperty("id",id);
		data.addProperty("time",time);
		return data.toString();
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject data = new JsonObject();
		data.addProperty("type",type);
		data.addProperty("id",id);
		data.addProperty("time",time);
		return data;
	}
}
