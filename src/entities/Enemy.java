package entities;

import java.awt.*;

public class Enemy {
    private int x, y;
    private int speed = 2;
    private int health = 3;

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update(Player player) {
        if (player.getX() > x) x += speed;
        if (player.getX() < x) x -= speed;
        if (player.getY() > y) y += speed;
        if (player.getY() < y) y -= speed;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, 32, 32);
    }

    public boolean collidesWith(int px, int py, int pw, int ph) {
        Rectangle r1 = new Rectangle(x, y, 32, 32);
        Rectangle r2 = new Rectangle(px, py, pw, ph);
        return r1.intersects(r2);
    }

    public void takeDamage(int dmg) {
        health -= dmg;
    }

    public boolean isAlive() {
        return health > 0;
    }

}