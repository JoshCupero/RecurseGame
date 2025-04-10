package entities;

import java.awt.*;
import javax.imageio.ImageIO;
import java.io.IOException;

import main.Game;
import main.InputHandler;
import stats.GameStats;

public class Player {
    private int x, y;
    private int speed = 4;
    private int health = 100;
    private Game game;
    private Image sprite;
    private int ammo = 20;
    private final int maxAmmo = 99;


    public Player(int x, int y, Game game) {
        this.x = x;
        this.y = y;
        this.game = game;

        try {
            sprite = ImageIO.read(getClass().getResource("/assets/player.png"));
        } catch (IOException e) {
            e.printStackTrace();
            sprite = null;
        }
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

            if (hasAmmo()) {
                game.shootProjectile(x + 16, y + 16, dx, dy);
                useAmmo();
                GameStats.shotsFired++;
            }
            
            InputHandler.shoot = false;
            GameStats.shotsFired++;
        }
    }

    public void draw(Graphics g) {
        if (sprite != null) {
            g.drawImage(sprite, x, y, 48, 64, null);
        } else {
            g.setColor(Color.CYAN);
            g.fillRect(x, y, 48, 64);
        }
    }

    public int getHealth() {
        return health;
    }

    public void takeDamage(int dmg) {
        if (canBeHit()) {
            health = Math.max(0, health - dmg);
            damageCooldown = 30;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private int damageCooldown = 0;

public void tickCooldown() {
    if (damageCooldown > 0) damageCooldown--;
}

public boolean canBeHit() {
    return damageCooldown <= 0;
}

public void heal(int amount) {
    health = Math.min(100, health + amount);
}

public boolean hasAmmo() {
    return ammo > 0;
}

public void useAmmo() {
    if (ammo > 0) ammo--;
}


public void addAmmo(int amount) {
    ammo = Math.min(ammo + amount, maxAmmo);
}

public int getAmmo() {
    return ammo;
}

}

