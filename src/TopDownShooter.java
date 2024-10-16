import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class TopDownShooter extends JPanel implements ActionListener {

    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private final int PLAYER_SIZE = 30;
    private final int BULLET_SIZE = 5;
    private final int BULLET_SPEED = 5;
    private final int PLAYER_SPEED = 5;
    private final int ENEMY_SIZE = 30;
    private final int ENEMY_SPEED = 2;

    private int playerX = WIDTH / 2;
    private int playerY = HEIGHT / 2;
    private final ArrayList<Bullet> bullets = new ArrayList<>();
    private final ArrayList<Enemy> enemies = new ArrayList<>();

    public TopDownShooter() {
        Timer timer = new Timer(20, this);
        timer.start();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_W) playerY -= PLAYER_SPEED;
                if (e.getKeyCode() == KeyEvent.VK_S) playerY += PLAYER_SPEED;
                if (e.getKeyCode() == KeyEvent.VK_A) playerX -= PLAYER_SPEED;
                if (e.getKeyCode() == KeyEvent.VK_D) playerX += PLAYER_SPEED;
                if (e.getKeyCode() == KeyEvent.VK_SPACE) shoot();
            }
        });

        // Spawn enemies
        for (int i = 0; i < 5; i++) {
            enemies.add(new Enemy((int) (Math.random() * WIDTH), (int) (Math.random() * HEIGHT)));
        }
    }

    private void shoot() {
        bullets.add(new Bullet(playerX + PLAYER_SIZE / 2 - BULLET_SIZE / 2, playerY));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.fillRect(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE);

        g.setColor(Color.RED);
        for (Bullet bullet : bullets) {
            g.fillRect(bullet.x, bullet.y, BULLET_SIZE, BULLET_SIZE);
        }

        g.setColor(Color.GREEN);
        for (Enemy enemy : enemies) {
            g.fillRect(enemy.x, enemy.y, ENEMY_SIZE, ENEMY_SIZE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Update bullets
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            bullet.y -= BULLET_SPEED;
            if (bullet.y < 0) {
                bullets.remove(i);
                i--;
            }
        }

        // Update enemies
        for (Enemy enemy : enemies) {
            if (enemy.x < playerX) enemy.x += ENEMY_SPEED;
            if (enemy.x > playerX) enemy.x -= ENEMY_SPEED;
            if (enemy.y < playerY) enemy.y += ENEMY_SPEED;
            if (enemy.y > playerY) enemy.y -= ENEMY_SPEED;
        }

        checkCollisions();
        repaint();
    }

    private void checkCollisions() {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            for (int j = 0; j < enemies.size(); j++) {
                Enemy enemy = enemies.get(j);
                if (bullet.x < enemy.x + ENEMY_SIZE && bullet.x + BULLET_SIZE > enemy.x &&
                        bullet.y < enemy.y + ENEMY_SIZE && bullet.y + BULLET_SIZE > enemy.y) {
                    bullets.remove(i);
                    enemies.remove(j);
                    i--;
                    break;
                }
            }
        }
    }

    private static class Bullet {
        int x, y;

        Bullet(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static class Enemy {
        int x, y;

        Enemy(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Top Down Shooter");
        TopDownShooter game = new TopDownShooter();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
