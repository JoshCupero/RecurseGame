package world;

import entities.Enemy;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Room {

    public void draw(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        for (int i = 0; i < 800; i += 40) {
            for (int j = 0; j < 600; j += 40) {
                g.drawRect(i, j, 40, 40);
            }
        }
    }

    public List<Enemy> spawnEnemies(int count) {
        List<Enemy> enemies = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int x = (int)(Math.random() * 700) + 50;
            int y = (int)(Math.random() * 500) + 50;
            enemies.add(new Enemy(x, y));
        }
        return enemies;
    }

    public ArrayList<Enemy> generateEnemies(int count) {
        ArrayList<Enemy> enemies = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            enemies.add(new Enemy(100 + i * 50, 100 + i * 50)); // Example positions
        }
        return enemies;
    }
}
