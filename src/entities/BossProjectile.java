package entities;

import java.awt.*;

public class BossProjectile {
    private int x, y, dx, dy;
    private boolean alive = true;

    public BossProjectile(int x, int y, int dx, int dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }

    public void update() {
        x += dx;
        y += dy;
        if (x < 0 || y < 0 || x > 1600 || y > 1200) alive = false;
    }

    public boolean collidesWith(Player player) {
        Rectangle r1 = new Rectangle(x, y, 32, 32);
        Rectangle r2 = new Rectangle(player.getX(), player.getY(), 48, 64);
        return r1.intersects(r2);
    }

    public void draw(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillOval(x, y, 32, 32);
    }

    public boolean isAlive() {
        return alive;
    }
}
