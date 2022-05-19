package lrk.mirairobot.main.threads;

import lrk.mirairobot.core.*;
import lrk.mirairobot.data.*;
import lrk.mirairobot.data.messagetype.*;
import lrk.mirairobot.utils.*;
import lrk.mirairobot.event.*;
import lrk.mirairobot.main.threads.RobotThread;

import java.lang.*;
import java.io.*;
import java.util.*;
import lrk.mirairobot.main.*;
import lrk.tools.miraiutils.*;

public class GroupMessageThread extends RobotThread{
	GroupMessageEvent event;
	public GroupMessageThread(GroupMessageEvent event){
		this.event = event;
		onload();
	}
	@Override
	public void run(){
		try{
			main();
		}catch(Exception e1){
			System.out.println(e1.getMessage());
		}
	}
	private void main() throws Exception{
		//event.getBotCore().nudge(MessageUtils.sendGroupNudge(event.getGroupID(),event.getSender()));
		if(event.getAtUsers().contains(3549850847L)){
    		String message = event.getMessage().split(" ")[1];
    		switch(message.split("&")[0]){
        		case "/pic":{
        			for(int i = 0;i < Integer.parseInt(message.split("&")[1].split("count=")[1]);i++){
            			int index = (int)(Math.random()*piclist.length);
        				if(piclist[index] != null){
        					event.reply(new Image(null,null,null,DataBridge.getFileBase64(piclist[index].getPath())));
        				}
    				}
    				break;
        		}
        		case "/AtAll":{
        			event.reply(new AtAll(),new Plain(message.split("&")[1].split("message=")[1]));
        		}
        		default:{
            		if(word_normal.containsKey(message)){
            			//尝试使用词库定义的图片回复
            			if(word_normal.getProperty(message).startsWith("pic:")){
            				String pic=word_normal.getProperty(message).split("pic:")[1];
            				Boolean tmp = false;
            				int index = 0;
            				for(int i = 0;i < pic_word.length;i++){
            					if((pic_word[i].getName()).equals(pic)){
            				 		tmp = true;
            				 		index = i;
            						break;
            					}
            				}
            				if(tmp){
            					event.reply(new Image(null,null,pic_word[index].getPath(),DataBridge.getFileBase64(pic_word[index].getPath())));
            				}else{
            					RobotNotification.Warnning("file lost");
            				}
            			}else{
            				event.reply(word_normal.get(message).toString());
            			}
            		}
        		}
    		}
    	}
    	String message = event.getMessage();
    	if(message.startsWith("/丢")){
        	if(event.getAtUsers().size()!=0){
        	    Iterator<Long> iterator = event.getAtUsers().iterator();
        	    while(iterator.hasNext()){
        	        event.reply(new Image(null,null,null,DataBridge.getImage(new 丢((long)iterator.next()))));
        	    }
        	}
    	}
    	if(message.startsWith("/爬")){
        	if(event.getAtUsers().size()!=0){
        	    Iterator<Long> iterator = event.getAtUsers().iterator();
        	    while(iterator.hasNext()){
        	        event.reply(new Image(null,null,null,DataBridge.getImage(new 爬((long)iterator.next()))));
        	    }
        	}
    	}
    	if(message.startsWith("/运势")){
	        event.reply(new Image(null,null,null,DataBridge.getImage(new 运势())));
    	}
    	
        sqldata.dispose();
	}
}
