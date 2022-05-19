package lrk.mirairobot.main.threads;

import lrk.mirairobot.core.*;
import lrk.mirairobot.data.*;
import lrk.mirairobot.utils.*;
import lrk.mirairobot.event.*;
import lrk.mirairobot.main.threads.RobotThread;

import java.lang.*;
import java.io.*;
import java.util.*;

public class FriendMessageThread extends RobotThread{
	FriendMessageEvent event;
	public FriendMessageThread(FriendMessageEvent event){
		this.event = event;
		onload();
	}
	@Override
	public void run(){
		try{
			main();
		}catch(IOException e1){
			System.out.println(e1.getMessage());
		}
	}
	private void main() throws IOException{
		if((event.getMessage()).equals("refresh")){
    		reload();
    		event.reply("完成");
    	}else{
    		if(word_normal.containsKey(event.getMessage())){
    			event.reply(word_normal.getProperty(event.getMessage()));
    		}
    	}
	}
}