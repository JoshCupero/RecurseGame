package entities;

import java.awt.*;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Item {
    public enum Type { AMMO, MEDKIT }

    private int x, y;
    private Type type;
    private Image sprite;

    public Item(int x, int y, Type type) {
        this.x = x;
        this.y = y;
        this.type = type;

        try {
            String path = type == Type.AMMO ? "/assets/ammo.png" : "/assets/medkit.png";
            sprite = ImageIO.read(getClass().getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g) {
        if (sprite != null)
            g.drawImage(sprite, x, y, 24, 24, null);
        else {
            g.setColor(type == Type.AMMO ? Color.YELLOW : Color.GREEN);
            g.fillRect(x, y, 24, 24);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 24, 24);
    }

    public Type getType() {
        return type;
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
