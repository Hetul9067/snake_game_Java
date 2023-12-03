import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;



public class SnakeGame extends JPanel implements ActionListener, KeyListener{



    private class Tile{
        int x;
        int y;

        Tile(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    int boardWidth;
    int boardHeight;
    int tileSize = 25;


//    status checking
    boolean gameRunning = true;
    JButton playButton;


    //Snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;


    //Food
    Tile food;

    Random random;

    //game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;


    SnakeGame(int boardWidth, int boardHeight){
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

//        snakeHead = new Tile(boardWidth/2/tileSize,boardHeight/2/tileSize);
        snakeHead = new Tile(10,10);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10,10);
        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 0;

        gameLoop = new Timer(100, this);
        gameLoop.start();

        playButton = new JButton("Play");
        playButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(gameRunning){
                    playButton.setVisible(false);
                }
                else if(gameOver || !gameRunning){
                    //Reset the game
                    resetGame();
                    gameLoop.start();
                    requestFocus();
                }
            }
        });
        add(playButton);
    }

    public void resetGame(){
//        snakeHead = new Tile(boardWidth/2/tileSize,boardHeight/2/tileSize);
        snakeHead = new Tile(10,10);
        snakeBody.clear();

        velocityX = 0;
        velocityY = 0;
        placeFood();
        gameOver = false;
        gameRunning = true;
        playButton.setVisible(false);
    }

    public void drawPlayButton(){

            playButton.setVisible(true);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            gbc.fill = GridBagConstraints.CENTER;
            JPanel buttonPanel = new JPanel(new GridBagLayout());
            buttonPanel.add(playButton, gbc);

            setLayout(new GridBagLayout());
            add(buttonPanel, gbc);


    }
//    public void drawPlayButton(){
//        playButton.setVisible(true);
//        playButton.setBounds(boardWidth /2 - (50), boardHeight / 2 - (15), 100, 30);
//        add(playButton);
//    }

    public void checkGameOver(){
        if(gameOver){
            gameRunning = false;
            drawPlayButton();
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        //Grid
//        for(int i=0; i<boardWidth/tileSize; i++){
//            //(x1, y1, x2, y2)
//            g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
//            g.drawLine(0, i*tileSize, boardWidth, i*tileSize);
//        }



        //Food
        g.setColor(Color.red);
//        g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);

        //Snake Head
        g.setColor(Color.green);
//        g.fillRect(snakeHead.x * tileSize, snakeHead.y* tileSize, tileSize, tileSize);
//        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y* tileSize, tileSize, tileSize, true);
        g.fillOval(snakeHead.x * tileSize, snakeHead.y* tileSize, tileSize, tileSize);

        //snake body
        for(int i=0; i< snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
            g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
        }

        //Score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if(gameOver){
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }else{
            g.drawString("Score: "+ String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
    }

    public void placeFood(){
        do {
            food.x = random.nextInt(boardWidth/ tileSize); //600/25 = 24
            food.y = random.nextInt(boardHeight/ tileSize); //600/25 = 24
        }while(snakeBody.contains(food) || collision(snakeHead, food));

    }



    public boolean collision(Tile tile1, Tile tile2){
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }
    public void move(){
        //eat food
        if(collision(snakeHead, food)){
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        //Snake Body
        for( int i= snakeBody.size()-1; i>=0; i--){
            Tile snakePart = snakeBody.get(i);
            if(i==0){
                snakePart.x = snakeHead.x;
                snakePart.y= snakeHead.y;
            }else{
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        //Snake Head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //game over conditions
        for(int i=0; i<snakeBody.size(); i++){
            Tile snakePart  = snakeBody.get(i);

            //collide with the snake head
            if(collision(snakeHead, snakePart)){
                gameOver = true;
            }
        }

        if(snakeHead.x*tileSize < 0 || snakeHead.x * tileSize > boardWidth ||
                snakeHead.y * tileSize< 0 || snakeHead.y * tileSize > boardHeight){
            gameOver = true;
        }

    }

    @Override
    public void actionPerformed(ActionEvent e){

        move();
        checkGameOver();
        repaint();
        if(gameOver){
            gameLoop.stop();
        }
    }


    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1){
            velocityX= 0;
            velocityY = -1;
        }else if(e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1){
            velocityX = 0;
            velocityY = 1;
        }else if(e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1){
            velocityX = -1;
            velocityY = 0;
        }else if(e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1){
            velocityX = 1;
            velocityY = 0;
        }
    }


    //do not need
    @Override
    public void keyTyped(KeyEvent e) {

    }


    @Override
    public void keyReleased(KeyEvent e) {

    }
}
