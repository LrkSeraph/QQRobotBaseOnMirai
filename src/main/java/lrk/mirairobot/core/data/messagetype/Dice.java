package lrk.mirairobot.core.data.messagetype;
//未知类型
import com.google.gson.*;

public class Dice extends Message{
	private final String type = "Dice";
	private int value;
	
	public Dice(int value){
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
	@Override
	public String toString(){
		JsonObject data = new JsonObject();
		data.addProperty("type",type);
		data.addProperty("value",value);
		return data.toString();
	}
	
	public JsonObject toJsonObject(){
		JsonObject data = new JsonObject();
		data.addProperty("type",type);
		data.addProperty("value",value);
		return data;
	}
}
