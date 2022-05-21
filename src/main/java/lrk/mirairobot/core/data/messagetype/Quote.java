package lrk.mirairobot.core.data.messagetype;

import com.google.gson.*;

public class Quote extends Message {
	private final String type = "Quote";
	private long id;//源消息id
	private long groupId;//源消息接收的群号(好友为0)
	private long senderId;//源消息的发送者
	private long targetId;//源消息接收者QQ号
	private JsonArray origin;//源消息内容

	public Quote(long id, long groupId, long senderId, long targetId, JsonArray origin) {
		this.id = id;
		this.groupId = groupId;
		this.senderId = senderId;
		this.targetId = targetId;
		this.origin = origin;
	}

	public long getId() {
		return id;
	}

	public long getGroupId() {
		return groupId;
	}

	public long getSenderId() {
		return senderId;
	}

	public long getTargetId() {
		return targetId;
	}

	public JsonArray getOrigin() {
		return origin;
	}

	@Override
	public String toString() {
		JsonObject data = new JsonObject();
		data.addProperty("type",type);
		data.addProperty("id",id);
		data.addProperty("groupId",groupId);
		data.addProperty("senderId",senderId);
		data.addProperty("targetId",targetId);
		data.add("origin",origin);
		return data.toString();
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject data = new JsonObject();
		data.addProperty("type",type);
		data.addProperty("id",id);
		data.addProperty("groupId",groupId);
		data.addProperty("senderId",senderId);
		data.addProperty("targetId",targetId);
		data.add("origin",origin);
		return data;
	}
}
