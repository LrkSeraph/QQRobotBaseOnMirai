package lrk.mirairobot.core.data.messagetype;

import com.google.gson.JsonObject;


public class Poke extends Message {
	private final String type = "Poke";
	private name name;

	public Poke(name name) {
		this.name = name;
	}
	
	public String getName(){
		return (String)name.name();
	}
	
	@Override
	public String toString() {
		JsonObject data = new JsonObject();
		data.addProperty("type",type);
		data.addProperty("name",name.name());
		return data.toString();
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject data = new JsonObject();
		data.addProperty("type",type);
		data.addProperty("name",name.name());
		return data;
	}
	
	public enum name{
		Poke,//戳一戳
		ShowLove,//比心
		Like,//点赞
		HeartBroken,//心碎
		SixSixSix,//666
		FangDaZhao//放大招
	}
}
