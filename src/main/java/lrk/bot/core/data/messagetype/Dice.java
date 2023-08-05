package lrk.bot.core.data.messagetype;

import com.google.gson.*;

//未知用途

@SuppressWarnings("unused")
public class Dice extends Message{
	private final String type = "Dice";
    private final int value;
	
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
