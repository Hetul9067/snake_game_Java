// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) throws Exception{
        int boardWidth = 600;
        int boardHeight = boardWidth;

        JFrame frame = new JFrame("Snake Assignment : Yasmeen + Hetul");
        SnakeGame snakeGame = new SnakeGame(boardWidth, boardHeight);


        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        frame.add(snakeGame);
        frame.pack();
        snakeGame.requestFocus();



    }

}