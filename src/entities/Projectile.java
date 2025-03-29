package entities;

import java.awt.*;

public class Projectile {
    private int x, y;
    private int dx, dy;
    private int lifespan = 60;

    public Projectile(int x, int y, int dx, int dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }

    public void update() {
        x += dx;
        y += dy;
    }
    

    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(x, y, 8, 8);
    }

    public boolean isAlive() {
        return lifespan > 0;
    }

    public boolean collidesWith(Enemy enemy) {
        return enemy.collidesWith(x, y, 8, 8);
    }
}
