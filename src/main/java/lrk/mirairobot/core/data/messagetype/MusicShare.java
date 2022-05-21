package lrk.mirairobot.core.data.messagetype;

import com.google.gson.JsonObject;

public class MusicShare extends Message {
	private final String type = "MusicShare";
	private String kind = "";//类型(可能是音乐类型)
	private String title = "";//音乐标题
	private String summary = "";//概括
	private String jumpUrl = "";//跳转URL
	private String pictureUrl = "";//封面图片URL
	private String musicUrl = "";//音源URL
	private String brief = "";//简介

	public MusicShare(String kind, String title, String summary, String jumpUrl, String pictureUrl, String musicUrl, String brief) {
		this.kind = kind;
		this.title = title;
		this.summary = summary;
		this.jumpUrl = jumpUrl;
		this.pictureUrl = pictureUrl;
		this.musicUrl = musicUrl;
		this.brief = brief;
	}


	public String getKind() {
		return kind;
	}

	public String getTitle() {
		return title;
	}

	public String getSummary() {
		return summary;
	}

	public String getJumpUrl() {
		return jumpUrl;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public String getMusicUrl() {
		return musicUrl;
	}

	public String getBrief() {
		return brief;
	}

	@Override
	public String toString() {
		JsonObject data = new JsonObject();
		data.addProperty("kind",kind);
		data.addProperty("title",title);
		data.addProperty("summary",summary);
		data.addProperty("jumpUrl",jumpUrl);
		data.addProperty("pictureUrl",pictureUrl);
		data.addProperty("musicUrl",musicUrl);
		data.addProperty("brief",brief);
		return data.toString();
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject data = new JsonObject();
		data.addProperty("kind",kind);
		data.addProperty("title",title);
		data.addProperty("summary",summary);
		data.addProperty("jumpUrl",jumpUrl);
		data.addProperty("pictureUrl",pictureUrl);
		data.addProperty("musicUrl",musicUrl);
		data.addProperty("brief",brief);
		return data;
	}
}
