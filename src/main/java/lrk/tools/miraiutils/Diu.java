package lrk.tools.miraiutils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public class Diu implements RobotImageBasedPlugin {

    BufferedImage logo, image;

    public Diu(long qq) throws IOException, URISyntaxException {
        logo = ImageIO.read(new URI("https://q1.qlogo.cn/g?b=qq&nk=%d&s=100".formatted(qq)).toURL());
        image = ImageIO.read(Objects.requireNonNull(Diu.class.getResourceAsStream("/assets/image/丢/diu.png")));
    }

    public BufferedImage getImage() {
        logo = ImageUtils.getCircleImage(logo);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(logo, 9, 177, 147, 147, null);
        g.dispose();
        return image;
    }

    public void dispose() {
        logo = null;
        image = null;
        System.gc();
    }
}