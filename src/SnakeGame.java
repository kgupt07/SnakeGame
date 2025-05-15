import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.*;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    private class Tile{
        int x, y;

        Tile(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    int frameWidth, frameHeight;
    int tileSize = 25;
    Random random;

    int velocityX = 1;
    int velocityY = 0;

    //Snake
    Tile snakeHead;
    ArrayList <Tile> snakeBody;

    //Food
    Tile food;

    //game logic
    Timer gameLoop;

    boolean gameOver = false;

    SnakeGame(int frameWidth, int frameHeight){
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        setPreferredSize(new Dimension(this.frameWidth, this.frameHeight));
        setBackground(Color.BLACK);

        snakeHead = new Tile(5, 5);
        food = new Tile(10, 10);
        snakeBody = new ArrayList<>();

        random = new Random();
        placeFood();
        placeSnake();

        gameLoop = new Timer(100, this);
        gameLoop.start();

        addKeyListener(this);
        setFocusable(true);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        //drawing Grid
        for (int i=0; i < frameWidth/tileSize; i++){
            g.drawLine(i*tileSize, 0, i*tileSize, frameHeight);
            g.drawLine(0, i*tileSize, frameWidth, i*tileSize);
        }

        //drawing food
        g.setColor(Color.red);
        g.fillRect(food.x, food.y, tileSize, tileSize);

        //drawing snake
        g.setColor(Color.green);
        g.fillRect(snakeHead.x, snakeHead.y, tileSize, tileSize);

        //snake body
        for (Tile snakePart : snakeBody) {
            g.fillRect(snakePart.x, snakePart.y, tileSize, tileSize);
        }

        g.setFont(new Font("Calibri", Font.BOLD, 16));
        if (gameOver){
            g.setColor(Color.red);
            g.drawString("Game Over: " + snakeBody.size(), tileSize-16, tileSize);
            g.drawString("Press Enter to restart", tileSize-16, tileSize+16);
        }
        else {
            g.setColor(Color.green);
            g.drawString("Score: " + snakeBody.size(), tileSize-16, tileSize);
        }
    }

    public void actionPerformed(ActionEvent e){
        move();
        repaint();
        if (gameOver){
            gameLoop.stop();
        }
    }

    public void move(){

        if (collided(snakeHead, food)){
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        for (int i = snakeBody.size() - 1; i >= 0; i--){
            Tile snakePart = snakeBody.get(i);
            if (i==0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else {
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        snakeHead.x += velocityX*25;
        snakeHead.y += velocityY*25;

        for (Tile snakePart : snakeBody) {
            if (collided(snakePart, snakeHead)) {
                gameOver = true;
            }
        }

        if (snakeHead.x < 0 || snakeHead.y < 0 ||
            snakeHead.x > frameWidth-tileSize || snakeHead.y > frameHeight-tileSize){
            gameOver = true;
        }
    }

    public void placeFood() {
        food.x = random.nextInt(frameWidth/tileSize) * 25;
        food.y = random.nextInt(frameHeight/tileSize) * 25;
    }

    public void placeSnake() {
        snakeHead.x = random.nextInt(frameWidth/tileSize) * 25;
        snakeHead.y = random.nextInt(frameHeight/tileSize) * 25;
    }

    public boolean collided (Tile t1, Tile t2) {
        return (t1.x == t2.x && t1.y == t2.y);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    public void resetGame() {
        snakeHead = new Tile(5, 5);
        snakeBody.clear();
        placeFood();
        placeSnake();
        gameOver = false;
        gameLoop.start(); // Restart the timer
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ENTER && gameOver){
            resetGame();
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1){
            velocityX = -1;
            velocityY = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1){
            velocityX = 1;
            velocityY = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1){
            velocityX = 0;
            velocityY = -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
