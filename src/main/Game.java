
package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import entities.*;
import world.*;
import stats.GameStats;

public class Game extends JPanel implements ActionListener, MouseMotionListener, MouseListener {
    private Timer timer;
    private Player player;
    private Room currentRoom;
    private ArrayList<Enemy> enemies;
    private ArrayList<Projectile> projectiles;
    private ArrayList<Item> items;
    private Image background;
    private int mouseX, mouseY;
    private int screenShake = 0;
    private boolean flashRed = false;
    private boolean inTitleScreen = true;
    private int gracePeriod = 120;
    private int currentRound = 1;
    private boolean betweenRounds = false;
    private boolean bossFightStarted = false;
    private ArrayList<BossProjectile> bossProjectiles = new ArrayList<>();
    private Image bossAlertImage;
    private boolean isGameOver = false;






    public Game() {
        setPreferredSize(new Dimension(800, 600));
        setFocusable(true);
        requestFocusInWindow();
    
        addMouseMotionListener(this);
        addMouseListener(this);
    
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (inTitleScreen && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    inTitleScreen = false;
                    startGame();
                }
            }
        });
    
        timer = new Timer(16, this);
        timer.start();
    }
    

    public void startGame() {
        System.out.println("Starting the game...");
        InputHandler inputHandler = new InputHandler();
        addKeyListener(inputHandler);
        requestFocus();

        player = new Player(400 - 24, 300 - 32, this);
        currentRoom = new Room();
        enemies = new ArrayList<>();
        projectiles = new ArrayList<>();
        items = new ArrayList<>();

        try {
            background = javax.imageio.ImageIO.read(getClass().getResource("/assets/background.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            bossAlertImage = javax.imageio.ImageIO.read(getClass().getResource("/assets/Terror of the ForestColosso Da MothBearRuler of the Cyborg Ninja Zombies.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        
        if (bossFightStarted && !enemies.isEmpty()) {
            if (bossAlertImage != null) {
                int imgWidth = bossAlertImage.getWidth(null);
                int x = (getWidth() - imgWidth) / 2;
                int y = 0;
                background.getGraphics().drawImage(bossAlertImage, x, y, null);
            } else {
                Graphics2D g2d = (Graphics2D) background.getGraphics();
                g2d.setFont(new Font("Monospaced", Font.BOLD, 32));
                g2d.setColor(Color.RED);
            }
        }
        

        spawnEnemies(5 + (2 * currentRound));
        int x = 100; 
        int y = 100; 
        Enemy.MovementType type = Enemy.MovementType.CHASER;
        Enemy e = new Enemy(x, y, type);
        e.loadDefaultSprite();
        inTitleScreen = false;
        gracePeriod = 120;
    }

    private void spawnEnemies(int count) {
        for (int i = 0; i < count; i++) {
            int x, y;
            int attempts = 0;
            do {
                x = (int)(Math.random() * 700);
                y = (int)(Math.random() * 500);
                attempts++;
                if (attempts > 100) break;
            } while (Enemy.isTooClose(x, y, enemies, 150) || new Rectangle(x, y, 64, 64).intersects(new Rectangle(376, 268, 48, 64)));

            Enemy.MovementType type = Enemy.MovementType.values()[(int)(Math.random() * Enemy.MovementType.values().length)];
            Enemy e = new Enemy(x, y, type);
            e.loadDefaultSprite();         
            e.setDropCallback((dropX, dropY) -> {
                double r = Math.random();
                if (r < 0.3) items.add(new Item(dropX, dropY, Item.Type.AMMO));
                else if (r < 0.6) items.add(new Item(dropX, dropY, Item.Type.MEDKIT));
            });
            enemies.add(e);
        }
    }

    private void spawnBoss() {
        Enemy boss = new Enemy(300, 100, Enemy.MovementType.CHASER); 
        boss.setDropCallback((x, y) -> {}); 
        enemies.add(boss);
        System.out.println("");
boss.setDropCallback((x, y) -> {});
enemies.add(boss);

    }
    
    public void start() {
        JFrame frame = new JFrame("CYBERROT: FOREST OF THE DAMNED");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        frame.setResizable(false);
        gd.setFullScreenWindow(frame);
        frame.setContentPane(this);
        frame.setVisible(true);
        requestFocus();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inTitleScreen) {
            repaint();
            return;
        }

        player.update();
        player.tickCooldown();

        if (gracePeriod > 0) gracePeriod--;

        for (Enemy enemy : enemies) {
            enemy.update(player, enemies);
        }

        if (player.getHealth() <= 0) {
            isGameOver = true;
            inTitleScreen = true;
        }
        

        for (Enemy enemy : enemies) {
            if (new Rectangle(enemy.getX(), enemy.getY(), 64, 64)
                    .intersects(new Rectangle(player.getX(), player.getY(), 48, 64))) {
                if (gracePeriod <= 0 && player.canBeHit()) {
                    player.takeDamage(10);
                    screenShake = 10;
                    flashRed = true;
                }
            }
        }

        Iterator<BossProjectile> bossIter = bossProjectiles.iterator();
while (bossIter.hasNext()) {
    BossProjectile blob = bossIter.next();
    blob.update();
    if (!blob.isAlive()) {
        bossIter.remove();
        continue;
    }
    if (blob.collidesWith(player)) {
        if (player.canBeHit()) {
            player.takeDamage(99);
            screenShake = 10;
            flashRed = true;
        }
        bossIter.remove();
    }
}


        Iterator<Item> itemIterator = items.iterator();
while (itemIterator.hasNext()) {
    Item item = itemIterator.next();
    Rectangle itemBounds = new Rectangle(item.getX(), item.getY(), 32, 32);
    Rectangle playerBounds = new Rectangle(player.getX(), player.getY(), 48, 64);
    if (itemBounds.intersects(playerBounds)) {
        item.collect(player);
        itemIterator.remove(); 
    }
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

enemies.removeIf(enemy -> !enemy.isAlive());

if (enemies.isEmpty() && !betweenRounds) {
    betweenRounds = true;

    Timer roundDelay = new Timer(2000, evt -> {
        if (currentRound == 20 && !bossFightStarted) {
            bossFightStarted = true;
            spawnBoss();
        } else {
            currentRound++;
            spawnEnemies(5 + currentRound);
        }
        betweenRounds = false;
        ((Timer) evt.getSource()).stop();
    });
    roundDelay.start();
}


        
        if (screenShake > 0) screenShake--;
        if (flashRed) flashRed = false;

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (inTitleScreen) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.RED);
            Font titleFont = new Font("Monospaced", Font.BOLD, 32);
            g.setFont(titleFont);
            FontMetrics fm = g.getFontMetrics(titleFont);
            String line1 = "CYBERROT";
            String line2 = "FOREST OF THE DAMNED";
            int x1 = (getWidth() - fm.stringWidth(line1)) / 2;
            int x2 = (getWidth() - fm.stringWidth(line2)) / 2;
            g.drawString(line1, x1, 200);
            g.drawString(line2, x2, 250);
            Font promptFont = new Font("Monospaced", Font.PLAIN, 20);
            g.setFont(promptFont);
            FontMetrics pfm = g.getFontMetrics(promptFont);
            String prompt = "Press ENTER to Start";
            int px = (getWidth() - pfm.stringWidth(prompt)) / 2;
            g.setColor(Color.WHITE);
            g.drawString(prompt, px, 350);
            return;
        }

        if (inTitleScreen && isGameOver) {
            enemies.clear();
            projectiles.clear();
            items.clear();
            bossProjectiles.clear();
            currentRound = 1;
            bossFightStarted = false;
            betweenRounds = false;
            isGameOver = false;
        }
        
        Graphics2D g2d = (Graphics2D) g.create();
        if (screenShake > 0) {
            int offsetX = (int)(Math.random() * 8 - 4);
            int offsetY = (int)(Math.random() * 8 - 4);
            g2d.translate(offsetX, offsetY);
        }

        if (background != null) {
            g2d.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        }

        currentRoom.draw(g2d);
        player.draw(g2d);
        for (Item item : items) item.draw(g2d);
        for (Enemy enemy : enemies) {
            enemy.draw(g2d);
            int barWidth = 64;
            int barHeight = 6;
            int barX = enemy.getX();
            int barY = enemy.getY() - 10;
            double healthPercent = Math.max(0, enemy.getHealth() / 3.0);
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRect(barX, barY, barWidth, barHeight);
            g2d.setColor(Color.RED);
            g2d.fillRect(barX, barY, (int)(barWidth * healthPercent), barHeight);
            g2d.setFont(new Font("Monospaced", Font.BOLD, 18));
            g2d.setColor(Color.WHITE);
            g2d.drawString("Ammo: " + player.getAmmo(), 20, getHeight() - 60);
            g2d.setFont(new Font("Monospaced", Font.BOLD, 20));
g2d.setColor(Color.WHITE);
g2d.drawString("Round: " + currentRound, getWidth() - 140, 30);
if (bossFightStarted && !enemies.isEmpty()) {
    g2d.setFont(new Font("Monospaced", Font.BOLD, 32));
    g2d.setColor(Color.RED);
    String bossText = "!!! BOSS FIGHT !!!";
    int textWidth = g2d.getFontMetrics().stringWidth(bossText);
    g2d.drawString(bossText, (getWidth() - textWidth) / 2, 0);
    for (BossProjectile bp : bossProjectiles) bp.draw(g2d);
    if (bossAlertImage != null) {
        int imgWidth = bossAlertImage.getWidth(null);
        g2d.drawImage(bossAlertImage, (getWidth() - imgWidth) / 2, 0, null);
    } else {
        g2d.setFont(new Font("Monospaced", Font.BOLD, 32));
        g2d.setColor(Color.RED);
        g2d.drawString(bossText, (getWidth() - textWidth) / 2, 0);
    }
    

}


        }
        for (Projectile p : projectiles) p.draw(g2d);

        g2d.setColor(Color.RED);
        g2d.drawLine(mouseX - 5, mouseY, mouseX + 5, mouseY);
        g2d.drawLine(mouseX, mouseY - 5, mouseX, mouseY + 5);

        int playerBarWidth = 200;
        int playerBarHeight = 20;
        int playerBarX = 20;
        int playerBarY = getHeight() - 40;
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(playerBarX, playerBarY, playerBarWidth, playerBarHeight);
        g2d.setColor(Color.GREEN);
        g2d.fillRect(playerBarX, playerBarY, (int)(playerBarWidth * (player.getHealth() / 100.0)), playerBarHeight);
        g2d.setColor(Color.WHITE);
        g2d.drawRect(playerBarX, playerBarY, playerBarWidth, playerBarHeight);

        g2d.dispose();

        if (flashRed) {
            g.setColor(new Color(255, 0, 0, 100));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public void shootProjectile(int x   , int y, int dx, int dy) {
        projectiles.add(new Projectile(x, y, dx, dy));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!inTitleScreen && e.getButton() == MouseEvent.BUTTON1) {
            int dx = mouseX - (player.getX() + 24);
int dy = mouseY - (player.getY() + 32); 
double length = Math.hypot(dx, dy);

dx = (int)(dx / length * 8);
dy = (int)(dy / length * 8);

            
                if (player.hasAmmo()) {
                    shootProjectile(player.getX() + 32, player.getY() + 32, dx, dy);
                    player.useAmmo(); 
                    GameStats.shotsFired++;
                }
            }
            
        }

        public void shootBossBlob(int x, int y, int dx, int dy) {
            bossProjectiles.add(new BossProjectile(x, y, dx, dy));
        }
        

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}