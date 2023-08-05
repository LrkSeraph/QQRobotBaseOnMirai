package lrk.bot.core.data.messagetype;

import com.google.gson.*;

//纯文本消息

@SuppressWarnings("unused")
public class Plain extends Message{

	private final String type = "Plain";
    private final String text;//文本内容
	
	public Plain(String text){
		this.text = text;
	}
	
	public String getText(){
		return this.text;
	}
	
	@Override
	public String toString(){
		JsonObject data = new JsonObject();
		data.addProperty("type",type);
		data.addProperty("text",text);
		return data.toString();
	}
	
	public JsonObject toJsonObject(){
		JsonObject data = new JsonObject();
		data.addProperty("type",type);
		data.addProperty("text",text);
		return data;
	}
}