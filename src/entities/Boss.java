package entities;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Boss {
    private int x, y;
    private BufferedImage sprite;
    private int width = 32;
    private int height = 32;

    public Boss(int x, int y) {
        this.x = x;
        this.y = y;
        try {
            sprite = ImageIO.read(getClass().getResource("/assets/Piranha.png"));
        } catch (IOException e) {
            e.printStackTrace();
            sprite = null;
        }
    }

    public void update() {
    }

    public void draw(Graphics g) {
        if (sprite != null) {
            g.drawImage(sprite, x, y, width, height, null);
        } else {
            g.setColor(Color.MAGENTA);
            g.fillRect(x, y, width, height);
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}