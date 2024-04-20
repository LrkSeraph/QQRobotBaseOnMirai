package lrk.tools.miraiutils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public class Pa implements RobotImageBasedPlugin {

    private BufferedImage logo, image;

    public Pa(long qq) throws IOException, URISyntaxException {
        logo = ImageIO.read(new URI("https://q1.qlogo.cn/g?b=qq&nk=%d&s=640".formatted(qq)).toURL());
        image = ImageIO.read(Objects.requireNonNull(Pa.class.getResourceAsStream("/assets/image/爬/爬%d.jpg".formatted((int) (Math.random() * 53)))));
    }

    public BufferedImage getImage() {
        logo = ImageUtils.getCircleImage(logo);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = image.getWidth();
        int h = image.getHeight();
        g.drawImage(logo, 0, h - h / 5, w / 5, h / 5, null);
        g.dispose();
        return image;
    }

    public void dispose() {
        logo = null;
        image = null;
        System.gc();
    }
}