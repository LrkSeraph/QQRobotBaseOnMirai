package lrk.mirairobot.event;

import lrk.mirairobot.core.BotCore;
import lrk.mirairobot.data.*;
import lrk.mirairobot.data.messagetype.*;
import lrk.mirairobot.utils.MessageUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.*;

public class GroupMessageEvent extends MessageEvent {
    // 群号
    private long groupID;
    // 群名称
    private String groupNickName;
    // 发送者QQ号
    private long senderID;
    // 发送者群名片
    private String senderNickName;
    // 消息顺序号(所有消息类型均有)
    private long messageId;
    // 消息发送时间(所有消息类型均有)
    private long messageTime;

    //@ 到的用户ID
    private ArrayList<Long> atUsers = new ArrayList<Long>();
    //消息中的图片
    private ArrayList<Image> pictures = new ArrayList<Image>();
    //App消息
    private App app;
    //文字消息
    private String message = "";
    
    public GroupMessageEvent(BotCore botCore, JsonObject data) {
        super(botCore,data);
        groupID = data.getAsJsonObject("sender").get("group").getAsJsonObject().get("id").getAsLong();
        groupNickName = data.getAsJsonObject("sender").get("group").getAsJsonObject().get("name").getAsString();
        senderID = data.getAsJsonObject("sender").get("id").getAsLong();
        senderNickName = data.getAsJsonObject("sender").get("memberName").getAsString();
        JsonArray messageChain = data.getAsJsonArray("messageChain");
        for(int i = 0;i < messageChain.size();i++){
        	JsonObject messagedata = messageChain.get(i).getAsJsonObject();
        	MessageType messageType= MessageType.valueOf(messagedata.get("type").getAsString());
        	switch(messageType){
        		case Source:{
        			messageId = messagedata.get("id").getAsLong();
        			messageTime  = messagedata.get("time").getAsLong();
        			break;
        		}
        		case Plain:{
        			message += messagedata.get("text").getAsString();
        			break;
        		}
        		case At:{
        			atUsers.add(messagedata.get("target").getAsLong());
        			break;
        		}
        		case Image:{
        			pictures.add(new Image(messagedata.get("imageId").getAsString(),messagedata.get("url").getAsString(),messagedata.get("path").getAsString(),messagedata.get("base64").getAsString()));
        			break;
        		}
        		case App:{
        		    app = new App(messagedata.get("content").getAsString());
        		    try{
        		    reply(app);
        		    }catch(Exception e){}
        		}
        	}
        }
    }

    @Override
    public JsonObject reply(String message) throws IOException {
        return getBotCore().sendGroupMessage(
                MessageUtils.sendMessageToGroup(groupID, new Plain(message))
        );
    }

	@Override
    public JsonObject reply(Message... message) throws IOException{
    	return getBotCore().sendGroupMessage(MessageUtils.sendMessageToGroup(groupID,message));
    }
    
    public long getGroupID() {
        return groupID;
    }

    public String getGroupNickName() {
        return groupNickName;
    }
    
    public long getSender(){
    	return senderID;
    }
    
    public String getSenderNickName() {
        return senderNickName;
    }

    public String getMessage() {
        return message;
    }

    public long getMessageId() {
        return messageId;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public ArrayList<Long> getAtUsers() {
        return atUsers;
    }

    public ArrayList<Image> getPictures() {
        return pictures;
    }
    
    @Override
    public String toString() {
        return "";
    }
}