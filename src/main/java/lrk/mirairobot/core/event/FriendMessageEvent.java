package lrk.mirairobot.core.event;

import lrk.mirairobot.core.BotCore;
import lrk.mirairobot.core.data.*;
import lrk.mirairobot.core.data.messagetype.*;
import lrk.mirairobot.utils.MessageUtils;
import com.google.gson.*;

import java.util.*;
import java.io.IOException;

public class FriendMessageEvent extends MessageEvent {
    // 消息id
    private long messageID;
    //消息中的文本
    private String message;
    //消息中的图片或大表情(PicMsg,BigFaceMsg)
    private ArrayList<Image> pictures;
    //非文本消息的表示形式(PicMsg,BigFaceMsg)
    private String tips;


    public FriendMessageEvent(BotCore botCore, JsonObject data) {
        super(botCore,data);
        
        senderID = data.getAsJsonObject("sender").get("id").getAsLong();
        JsonArray messageChain = data.getAsJsonArray("messageChain");
        for(int i = 0;i < messageChain.size();i++){
        	JsonObject messagedata = messageChain.get(i).getAsJsonObject();
        	MessageType messageType= MessageType.valueOf(messagedata.get("type").getAsString());
        	switch(messageType){
        		case Source:{
        			messageID = messagedata.get("id").getAsLong();
        			break;
        		}
        		case Plain:{
        			message = messagedata.get("text").getAsString();
        			break;
        		}
        	}
        }

    }

    @Override
    public JsonObject reply(String message) throws IOException {
        return getBotCore().sendFriendMessage(
                MessageUtils.sendMessageToFriend(senderID, new Plain(message))
        );
    }
    
    @Override
    public JsonObject reply(Message... message) throws IOException{
    	return getBotCore().sendFriendMessage(MessageUtils.sendMessageToFriend(senderID,message));
    }

    public String getMessage() {
        return message;
    }

    public long getMessageID() {
        return messageID;
    }

    public ArrayList<Image> getPictures() {
        return pictures;
    }

    public String getTips() {
        return tips;
    }

    @Override
    public String toString() {
        return "";
    }
}