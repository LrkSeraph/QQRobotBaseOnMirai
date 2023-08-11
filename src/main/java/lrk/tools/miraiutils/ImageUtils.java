package lrk.tools.miraiutils;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class ImageUtils {
    public static BufferedImage getCircleImage(BufferedImage rawImage) {
        int width = rawImage.getWidth();
        BufferedImage image = new BufferedImage(width, width, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Ellipse2D.Double shape = new Ellipse2D.Double(0, 0, width, width);
        g.setClip(shape);
        g.drawImage(rawImage, 0, 0, width, width, null);
        return image;
    }
}