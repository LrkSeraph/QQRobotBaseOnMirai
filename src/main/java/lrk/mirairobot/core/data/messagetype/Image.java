package lrk.mirairobot.core.data.messagetype;

import com.google.gson.*;

/**
 * 消息中所携带的图片
 */
public class Image extends Message{
	private String type = "Image";
	private String imageId;//图片ID
	private String url;//图片URL
	private String path;//图片本地路径
	private String base64;//图片的Base64编码
	
	public Image(String imageId,String url,String path,String base64){
		this.imageId = imageId;
		this.url = url;
		this.path = path;
		this.base64 = base64;
	}
	public String getType(){
		return type;
	}
	public String getImageId(){
		return imageId;
	}
	public String getUrl(){
		return url;
	}
	public String getPath(){
		return path;
	}
	public String getBase64(){
		return base64;
	}
	
	@Override
	public String toString(){
		JsonObject data = new JsonObject();
		data.addProperty("type",type);
		data.addProperty("imageId",imageId);
		data.addProperty("url",url);
		data.addProperty("path",path);
		data.addProperty("base64",base64);
		return data.toString();
	}
	public JsonObject toJsonObject(){
		JsonObject data = new JsonObject();
		data.addProperty("type",type);
		data.addProperty("imageId",imageId);
		data.addProperty("url",url);
		data.addProperty("path",path);
		data.addProperty("base64",base64);
		return data;
	}
}
