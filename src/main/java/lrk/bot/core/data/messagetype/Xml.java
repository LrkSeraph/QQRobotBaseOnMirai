package lrk.bot.core.data.messagetype;

import com.google.gson.JsonObject;

//Xml卡片消息

@SuppressWarnings("unused")
public class Xml extends Message {
	private final String type = "Xml";
    private final String xml;//XML消息内容

	public Xml(String xml) {
		this.xml = xml;
	}

	public String getXml() {
		return xml;
	}
	
	@Override
	public String toString() {
		JsonObject data = new JsonObject();
		data.addProperty("type",type);
		data.addProperty("xml",xml);
		return data.toString();
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject data = new JsonObject();
		data.addProperty("type",type);
		data.addProperty("xml",xml);
		return data;
	}
}
