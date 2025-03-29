package main;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.Timer;

import entities.Enemy;
import entities.Player;
import entities.Projectile;
import world.Room;

import javax.swing.JPanel;
import javax.swing.JFrame;

public class Game extends JPanel implements ActionListener {
    private Timer timer;
    private Player player;
    private Room currentRoom;
    private ArrayList<Enemy> enemies;
    private ArrayList<Projectile> projectiles;

    public Game() {
        setPreferredSize(new Dimension(800, 600));
        setFocusable(true);
        InputHandler inputHandler = new InputHandler();
        addKeyListener(inputHandler);

        player = new Player(100, 100, this);
        currentRoom = new Room();
        enemies = new ArrayList<>(currentRoom.generateEnemies(5)); // or any number        
        enemies = new ArrayList<>();
        projectiles = new ArrayList<>();

        // Spawn some enemies for demo
        enemies.add(new Enemy(400, 300));
        enemies.add(new Enemy(600, 200));

        timer = new Timer(16, this); // ~60 FPS
    }

    public void start() {
        JFrame frame = new JFrame("Recurse");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        player.update();

        for (Enemy enemy : enemies) {
            enemy.update(player);
        }

        Iterator<Projectile> projIterator = projectiles.iterator();
        while (projIterator.hasNext()) {
            Projectile p = projIterator.next();
            p.update();
            if (!p.isAlive()) {
                projIterator.remove();
                continue;
            }
            for (Enemy enemy : enemies) {
                if (p.collidesWith(enemy)) {
                    enemy.takeDamage(1);
                    projIterator.remove();
                    break;
                }
            }
        }

        enemies.removeIf(enemy -> !enemy.isAlive());

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        currentRoom.draw(g);
        player.draw(g);
        for (Enemy enemy : enemies) enemy.draw(g);
        for (Projectile p : projectiles) p.draw(g);
    }

    public void shootProjectile(int x, int y, int dx, int dy) {
        projectiles.add(new Projectile(x, y, dx, dy));
    }
}