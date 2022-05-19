package lrk.tools.miraiutils;

import java.io.*;
import java.net.*;
import java.awt.image.*;
import java.awt.*;
import javax.imageio.ImageIO;

public class 爬 implements Utils{

    private long qq;
    private BufferedImage logo,image;
    
    public 爬(long qq) throws IOException{
        this.qq = qq;
        logo = ImageIO.read(new URL("https://q1.qlogo.cn/g?b=qq&nk="+qq+"&s=640"));
        image = ImageIO.read(爬.class.getResourceAsStream("/assets/image/爬/爬"+(int)(Math.random()*53)+".jpg"));
    }
    
    public BufferedImage getImage(){
        logo = Tool.getCircleImage(logo);
        Graphics2D g = (Graphics2D)image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        int w = image.getWidth();
        int h = image.getHeight();
        g.drawImage(logo,0,h-h/5,w/5,h/5,null);
        g.dispose();
        return image;
    }
    
    public void dispose(){
        logo = null;
        image = null;
        System.gc();
    }
}