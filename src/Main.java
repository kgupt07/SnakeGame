import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        int frameWidth = 600;
        int frameHeight = 600;

        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setSize(frameWidth, frameHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        SnakeGame game = new SnakeGame(frameWidth, frameHeight);
        frame.add(game);
        frame.pack();
        game.requestFocus();

    }
}