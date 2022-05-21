package lrk.mirairobot.core.data.messagetype;
//表情消息
import com.google.gson.*;

public class Face extends Message{
	private final String type = "Face";
	private int faceId = 0;//表情编号
	private String name = "";//表情拼音
	
	public Face(int faceId,String name){
		this.faceId = faceId;
		this.name = name;
	}
	
	public int getFaceId(){
		return faceId;
	}
	
	public String getName(){
		return name;
	}
	
	@Override
	public String toString(){
		JsonObject data = new JsonObject();
		data.addProperty("type",type);
		data.addProperty("faceId",faceId);
		data.addProperty("name",name);
		return data.toString();
	}
	
	public JsonObject toJsonObject(){
		JsonObject data = new JsonObject();
		data.addProperty("type",type);
		data.addProperty("faceId",faceId);
		data.addProperty("name",name);
		return data;
	}
}