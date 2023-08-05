package lrk.bot.core.data.messagetype;

import com.google.gson.JsonObject;

@SuppressWarnings("unused")
public class MiraiCode extends Message {
	private final String type = "MiraiCode";
    private final String code;

	public MiraiCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
	@Override
	public String toString() {
		JsonObject data = new JsonObject();
		data.addProperty("code",code);
		return data.toString();
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject data = new JsonObject();
		data.addProperty("code",code);
		return data;
	}
	
	
}
