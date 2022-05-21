package lrk.mirairobot.core.data.messagetype;
//@全体成员
import com.google.gson.*;

public class AtAll extends Message{
	private final String type = "AtAll";
	
	public AtAll(){
		
	}
	
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