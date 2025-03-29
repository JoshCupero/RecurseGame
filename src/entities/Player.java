package entities;

import java.awt.*;

import main.Game;
import main.InputHandler;
import stats.GameStats;

public class Player {
    private int x, y;
    private int speed = 4;
    private Game game;

    public Player(int x, int y, Game game) {
        this.x = x;
        this.y = y;
        this.game = game;
    }

    public void update() {
        if (InputHandler.up) y -= speed;
        if (InputHandler.down) y += speed;
        if (InputHandler.left) x -= speed;
        if (InputHandler.right) x += speed;

        if (InputHandler.shoot) {
            int dx = 0, dy = 0;
            if (InputHandler.up) dy = -8;
            else if (InputHandler.down) dy = 8;
            else if (InputHandler.left) dx = -8;
            else if (InputHandler.right) dx = 8;
            else dy = -8;

            game.shootProjectile(x + 16, y + 16, dx, dy);
            InputHandler.shoot = false;
            GameStats.shotsFired++;
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillRect(x, y, 32, 32); // Temporary square sprite
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
