package lrk.mirairobot.core.data.messagetype;

import com.google.gson.*;

public class File extends Message {
	private final String type = "File";
	private int id = 0;
	private String name = "";
	private int size = 0;
	
	public File(int id,String name,int size){
		this.id = id;
		this.name = name;
		this.size = size;
	}

	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getSize() {
		return size;
	}
	
	@Override
	public String toString() {
		JsonObject data = new JsonObject();
		data.addProperty("type",type);
		data.addProperty("id",id);
		data.addProperty("name",name);
		data.addProperty("size",size);
		return data.toString();
	}
	

	@Override
	public JsonObject toJsonObject() {
		JsonObject data = new JsonObject();
		data.addProperty("type",type);
		data.addProperty("id",id);
		data.addProperty("name",name);
		data.addProperty("size",size);
		return data;
	}
	
	
	
}
