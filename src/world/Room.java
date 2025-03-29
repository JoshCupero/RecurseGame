// Room.java
package world;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import entities.Enemy;

public class Room {
    public void draw(Graphics g) {
        // Optional: draw room-specific stuff here
    }

    public List<Enemy> generateEnemies(int count) {
        List<Enemy> enemies = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int x = (int)(Math.random() * 700);
            int y = (int)(Math.random() * 500);
            Enemy.MovementType type = Enemy.MovementType.values()[(int)(Math.random() * Enemy.MovementType.values().length)];
            enemies.add(new Enemy(x, y, type));
        }
        return enemies;
    }

    
    public void spawnEnemies(int count) {
        // Logic to spawn 'count' number of enemies
        System.out.println(count + " enemies spawned.");
    }
}
