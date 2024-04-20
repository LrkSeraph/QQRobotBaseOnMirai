package lrk.tools.miraiutils;

import java.awt.image.BufferedImage;

public interface RobotImageBasedPlugin {
    BufferedImage getImage();
    void dispose();
}