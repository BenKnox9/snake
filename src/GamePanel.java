import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//  --> shift-option-k

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 40;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 175;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 2;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();

    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if (running) {

            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                // draw head
                if (i == 0) {
                    g.setColor(Color.green);
                    // g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    g.fillRoundRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE, (3 * UNIT_SIZE) / 4, (3 * UNIT_SIZE) / 4);
                    switch (direction) {
                        case 'U':
                            g.fillRect(x[i], y[i] + UNIT_SIZE / 2, UNIT_SIZE, UNIT_SIZE / 2);
                            g.setColor(Color.black);
                            g.fillOval(x[i] + 5, y[i] + UNIT_SIZE / 5, UNIT_SIZE / 6, UNIT_SIZE / 6);
                            g.fillOval(x[i] + (2 * UNIT_SIZE) / 3, y[i] + UNIT_SIZE / 5, UNIT_SIZE / 6, UNIT_SIZE / 6);
                            break;
                        case 'D':
                            g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE / 2);
                            g.setColor(Color.black);
                            g.fillOval(x[i] + 5, y[i] + UNIT_SIZE / 2, UNIT_SIZE / 6, UNIT_SIZE / 6);
                            g.fillOval(x[i] + (2 * UNIT_SIZE) / 3, y[i] + UNIT_SIZE / 2, UNIT_SIZE / 6, UNIT_SIZE / 6);
                            break;
                        case 'L':
                            g.fillRect(x[i] + UNIT_SIZE / 2, y[i], UNIT_SIZE / 2, UNIT_SIZE);
                            g.setColor(Color.black);
                            g.fillOval(x[i] + 5, y[i] + UNIT_SIZE / 5, UNIT_SIZE / 6, UNIT_SIZE / 6);
                            g.fillOval(x[i] + (2 * UNIT_SIZE) / 3, y[i] + UNIT_SIZE / 5, UNIT_SIZE / 6, UNIT_SIZE / 6);
                            break;
                        case 'R':
                            g.fillRect(x[i], y[i], UNIT_SIZE / 2, UNIT_SIZE);
                            g.setColor(Color.black);
                            g.fillOval(x[i] + 5, y[i] + UNIT_SIZE / 5, UNIT_SIZE / 6, UNIT_SIZE / 6);
                            g.fillOval(x[i] + (2 * UNIT_SIZE) / 3, y[i] + UNIT_SIZE / 5, UNIT_SIZE / 6, UNIT_SIZE / 6);
                            break;
                    }
                } else {
                    // draw body
                    // g.setColor(new Color(45, 180, 0));
                    if (i % 2 == 0) {
                        g.setColor(new Color(0, random.nextInt(165) + 90, 0));
                        // g.setColor(Color.green);
                        g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    } else {
                        g.setColor(new Color(0, random.nextInt(165) + 90, 0));
                        // g.setColor(new Color(45, 180, 0));
                        g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    }
                }
            }

            g.setColor(Color.red);
            g.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten,
                    (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
                    g.getFont().getSize());

        } else {
            gameOver(g);
        }

    }

    public void newApple() {
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        for (int i = 0; i < bodyParts; i++) {
            while (appleX == x[i]) {
                appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
            }
        }
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
        for (int i = 0; i < bodyParts; i++) {
            while (appleY == y[i]) {
                appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
            }
        }
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];

        }
        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }

    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();

        }
    }

    public void checkCollisions() {
        // checks if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        // checks if head collides with left border
        if (x[0] < 0) {
            running = false;
        }
        // checks if head collides with right border
        if (x[0] > SCREEN_WIDTH - UNIT_SIZE) {
            running = false;
        }
        // checks if head collides with top border
        if (y[0] < 0 - UNIT_SIZE) {
            running = false;
        }
        // checks if head collides with bottom border
        if (y[0] > SCREEN_HEIGHT - UNIT_SIZE) {
            running = false;
        }
        if (!running)
            timer.stop();

    }

    public void gameOver(Graphics g) {
        // game over text
        g.setColor(Color.red);
        g.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
        // score
        g.setColor(Color.red);
        g.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten,
                (SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten)) / 2,
                g.getFont().getSize());

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();

        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }

        }
    }
}
