package lrk.bot.core.data.messagetype;

import com.google.gson.*;

//应用消息(轻视频等)

@SuppressWarnings("unused")
public class App extends Message{
	private final String type = "App";
	private final String content;//内容
	public JsonObject appContent;
	public App(String content){
		this.content = content;
		this.appContent = new JsonObject();
		appContent.addProperty("content",content);
	}
	
	public String getContent(){
		return content;
	}
	
	public JsonObject getAppContent(){
	    return appContent;
	}
	@Override
	public String toString(){
		JsonObject data = new JsonObject();
		data.addProperty("type",type);
		data.addProperty("content",content);
		return data.toString();
	}
	
	public JsonObject toJsonObject(){
		JsonObject data = new JsonObject();
		data.addProperty("type",type);
		data.addProperty("content",content);
		return data;
	}
}
