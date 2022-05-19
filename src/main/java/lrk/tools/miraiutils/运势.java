package lrk.tools.miraiutils;

import java.io.*;
import java.util.*;
import java.awt.image.BufferedImage;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.imageio.ImageIO;

import com.google.gson.*;

public class 运势 implements Utils{
    
    private Font 微软雅黑,Sakura;
    private final int font_title_size = 45,font_text_size = 25;
    private BufferedImage image;
    private Color title_color,text_color;
    private JsonArray title,text;
    
    public 运势() throws FontFormatException,IOException{
        微软雅黑 = Font.createFont(Font.TRUETYPE_FONT,运势.class.getResourceAsStream("/assets/font/微软雅黑.ttf"));//.deriveFont(font_title_size);
        Sakura = Font.createFont(Font.TRUETYPE_FONT,运势.class.getResourceAsStream("/assets/font/Sakura.ttf"));//.deriveFont(font_text_size);
        image = ImageIO.read(运势.class.getResourceAsStream("/assets/image/运势/frame_"+(int)(Math.random()*65+1)+".jpg"));
        title_color = Color.decode("#F5F5F5");
        text_color = Color.decode("#323232");
        JsonObject tmp = (JsonObject)JsonParser.parseString(new String(运势.class.getResourceAsStream("/assets/json/运势/title.json").readAllBytes()));
        title = (JsonArray)tmp.get("types_of").getAsJsonArray();
        tmp = (JsonObject)JsonParser.parseString(new String(运势.class.getResourceAsStream("/assets/json/运势/text.json").readAllBytes()));
        text = (JsonArray)tmp.get("copywriting").getAsJsonArray();
        
    }
    
    public BufferedImage getImage(){
        //获取数据
        JsonObject title_s = (JsonObject)title.get((int)(Math.random()*(title.size()-1))).getAsJsonObject();
        int luck = title_s.get("good-luck").getAsInt();
        String 运势 = title_s.get("name").getAsString();
        String 祝福 = "";
        Iterator<JsonElement> iterator = text.iterator();
        while(iterator.hasNext()){
            JsonObject obj = (JsonObject)iterator.next();
            if(obj.get("good-luck").getAsInt() == luck){
                祝福 = obj.get("content").getAsString();
                break;
            }
        }
        Graphics2D g = (Graphics2D)image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        //绘制Title
        g.setFont(new Font("宋体",Font.BOLD,45));
        g.setColor(title_color);
        switch(运势.length()){
            case 1:{
                g.drawString(运势,115,116);
                break;
            }
            case 2:{
                g.drawString(运势,95,116);
                break;
            }
            case 3:{
                g.drawString(运势,75,116);
                break;
            }
        }
        //绘制Text
        g.setFont(new Font("微软雅黑",Font.BOLD,25));
        g.setColor(text_color);
        drawString_s(g,85,200,祝福);
        return image;
    }
    private void drawString_s(Graphics2D g,int x,int y,String str){
        int index = 0;
        int y_raw = y;
        for(String subString:str.split("")){
            g.drawString(subString,x,y);
            y+=g.getFontMetrics().stringWidth(subString);
            if(++index>8){
                x+=g.getFontMetrics().stringWidth(subString)+1;
                index = 0;
                y = y_raw;
            }
        }
    }
    public void dispose(){
        image = null;
        System.gc();
    }
}