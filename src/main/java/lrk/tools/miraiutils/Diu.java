package lrk.tools.miraiutils;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;
import javax.imageio.ImageIO;

public class Diu implements Utils{

    BufferedImage logo,image;
    
    public Diu(long qq) throws IOException{
        logo = ImageIO.read(new URL("https://q1.qlogo.cn/g?b=qq&nk="+qq+"&s=100"));
        image = ImageIO.read(Objects.requireNonNull(Diu.class.getResourceAsStream("/assets/image/丢/diu.png")));
    }
    
    public BufferedImage getImage(){
        logo = Tool.getCircleImage(logo);
        Graphics2D g = (Graphics2D)image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(logo,9,177,147,147,null);
        g.dispose();
        return image;
    }
    
    public void dispose(){
        logo = null;
        image = null;
        System.gc();
    }
}