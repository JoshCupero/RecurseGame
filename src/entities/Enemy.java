// Enemy.java
package entities;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.List;

public class Enemy {
    public enum MovementType {
        CHASER, ZIGZAGGER, WANDERER
    }

    private int x, y;
    private int speed = 2;
    private int health = 3;
    private BufferedImage sprite;
    private DropCallback dropCallback;
    private MovementType type;
    private int zigzagTimer = 0;
    private int wanderTimer = 0;

    public Enemy(int x, int y, MovementType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        try {
            sprite = ImageIO.read(getClass().getResource("/assets/enemy.png"));
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            sprite = null;
        }
    }

    public static boolean isTooClose(int x, int y, List<Enemy> others, int minDistance) {
        for (Enemy other : others) {
            double dist = Math.hypot(x - other.x, y - other.y);
            if (dist < minDistance) {
                return true;
            }
        }
        return false;
    }

    public void update(Player player, List<Enemy> others) {
        int dx = 0, dy = 0;

        switch (type) {
            case CHASER:
                if (player.getX() > x) dx = speed;
                if (player.getX() < x) dx = -speed;
                if (player.getY() > y) dy = speed;
                if (player.getY() < y) dy = -speed;
                break;
            case ZIGZAGGER:
                zigzagTimer++;
                if (player.getX() > x) dx = speed;
                if (player.getX() < x) dx = -speed;
                if (zigzagTimer % 20 < 10) {
                    dy = (Math.random() > 0.5) ? speed : -speed;
                }
                break;
            case WANDERER:
                wanderTimer++;
                if (distanceTo(player) < 200) {
                    if (player.getX() > x) dx = speed;
                    if (player.getX() < x) dx = -speed;
                    if (player.getY() > y) dy = speed;
                    if (player.getY() < y) dy = -speed;
                } else if (wanderTimer % 30 == 0) {
                    dx = (int)(Math.random() * 3 - 1) * speed;
                    dy = (int)(Math.random() * 3 - 1) * speed;
                }
                break;
        }

        moveWithCollision(dx, dy, others);
    }

    private void moveWithCollision(int dx, int dy, List<Enemy> others) {
        int futureX = x + dx;
        int futureY = y + dy;

        boolean xClear = true;
        boolean yClear = true;

        for (Enemy other : others) {
            if (other == this) continue;
            Rectangle rx = new Rectangle(futureX, y, 64, 64);
            Rectangle ry = new Rectangle(x, futureY, 64, 64);
            Rectangle ro = new Rectangle(other.x, other.y, 64, 64);

            if (rx.intersects(ro)) xClear = false;
            if (ry.intersects(ro)) yClear = false;
        }

        if (xClear) x = futureX;
        if (yClear) y = futureY;
    }

    private double distanceTo(Player p) {
        return Math.hypot(p.getX() - x, p.getY() - y);
    }

    public void draw(Graphics g) {
        if (sprite != null) {
            g.drawImage(sprite, x, y, 64, 64, null);
        } else {
            g.setColor(Color.RED);
            g.fillRect(x, y, 64, 64);
        }
    }

    public boolean collidesWith(int px, int py, int pw, int ph) {
        Rectangle r1 = new Rectangle(x, y, 64, 64);
        Rectangle r2 = new Rectangle(px, py, pw, ph);
        return r1.intersects(r2);
    }

    public void takeDamage(int dmg) {
        health -= dmg;
        if (health <= 0 && dropCallback != null) {
            dropCallback.dropItem(x, y);
        }
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void setDropCallback(DropCallback callback) {
        this.dropCallback = callback;
    }

    public interface DropCallback {
        void dropItem(int x, int y);
    }

    public int getHealth() {
    return health;
}
    public int getX() { return x; }
    public int getY() { return y; }
}
