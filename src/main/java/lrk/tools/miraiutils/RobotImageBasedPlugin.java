package lrk.tools.miraiutils;

import java.awt.image.BufferedImage;

public interface RobotImageBasedPlugin {
    public BufferedImage getImage();
    public void dispose();
}