package lrk.mirairobot.core.data.messagetype;

import com.google.gson.JsonObject;

public class Voice extends Message {
	private final String type = "Voice";
	private String voiceId = "";//语音消息识别id
	private String url = "";//语音URL
	private String path = "null";//语音本地路径
	private String base64 = "null";//语音的Base64编码
	private long length;//语音长度(发送时可以不写)

	public Voice(String voiceId, String url, String path, String base64, long length) {
		this.voiceId = voiceId;
		this.url = url;
		this.path = path;
		this.base64 = base64;
		this.length = length;
	}
	
	public Voice(String voiceId, String url, String path, String base64) {
		this.voiceId = voiceId;
		this.url = url;
		this.path = path;
		this.base64 = base64;
		this.length = 0;
	}

	public String getVoiceId() {
		return voiceId;
	}

	public String getUrl() {
		return url;
	}

	public String getPath() {
		return path;
	}

	public String getBase64() {
		return base64;
	}
	
	@Override
	public String toString() {
		JsonObject data = new JsonObject();
		data.addProperty("type",type);
		data.addProperty("voiceId",voiceId);
		data.addProperty("url",url);
		data.addProperty("path",path);
		data.addProperty("base64",base64);
		data.addProperty("length",length);
		return data.toString();
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject data = new JsonObject();
		data.addProperty("type",type);
		data.addProperty("voiceId",voiceId);
		data.addProperty("url",url);
		data.addProperty("path",path);
		data.addProperty("base64",base64);
		data.addProperty("length",length);
		return data;
	}
}
