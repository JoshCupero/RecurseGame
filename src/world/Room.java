package world;

import java.awt.*;

public class Room {
    public void draw(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        for (int i = 0; i < 800; i += 40) {
            for (int j = 0; j < 600; j += 40) {
                g.drawRect(i, j, 40, 40);
            }
        }
    }
}