package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePlay extends JPanel implements KeyListener, ActionListener {

  private boolean play = false;
  private int score = 0;

  private int totalBricks = 21;

  private Timer timer;
  private int delay = 1;

  private int playerX = 310;

  private int ballposX = 120;
  private int ballposY = 350;
  private int ballXdir = -1;
  private int ballYdir = -2;

  private MapGenerator mapGenerator;

  public GamePlay() {
    mapGenerator = new MapGenerator(3, 7);
    addKeyListener(this);
    setFocusable(true);
    setFocusTraversalKeysEnabled(false);
    timer = new Timer(delay, this);
    timer.start();
  }

  public void paint(Graphics graphics) {
    // background
    graphics.setColor(Color.BLACK);
    graphics.fillRect(1, 1, 692, 592);

    // drawing map
    mapGenerator.draw((Graphics2D) graphics);

    // borders
    graphics.setColor(Color.yellow);
    graphics.fillRect(0, 0, 3, 592);
    graphics.fillRect(0, 0, 692, 3);
    graphics.fillRect(691, 0, 3, 592);

    // score
    graphics.setColor(Color.white);
    graphics.setFont(new Font("serif", Font.BOLD, 25));
    graphics.drawString("Point  " + score, 590, 30);

    // the paddle
    graphics.setColor(Color.green);
    graphics.fillRect(playerX, 550, 100, 8);

    if (totalBricks <= 0){
      play = false;
      ballXdir = 0;
      ballYdir = 0;
      graphics.setColor(Color.green);
      graphics.setFont(new Font("serif", Font.BOLD, 30));
      graphics.drawString("You Won: " + score, 260, 300);

      graphics.setFont(new Font("serif", Font.BOLD, 20));
      graphics.drawString("Press Enter to Restart", 230, 350);
    }

    // the ball
    graphics.setColor(Color.yellow);
    graphics.fillOval(ballposX, ballposY, 20, 20);
    if (ballposY > 570){
      play = false;
      ballXdir = 0;
      ballYdir = 0;
      graphics.setColor(Color.red);
      graphics.setFont(new Font("serif", Font.BOLD, 30));
      graphics.drawString("Game Over, Scores: " + score, 190, 300);

      graphics.setFont(new Font("serif", Font.BOLD, 20));
      graphics.drawString("Press Enter to Restart", 230, 350);
    }

    graphics.dispose();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    timer.start();

    if (play) {
      if (new Rectangle(ballposX, ballposY, 20, 20)
          .intersects(new Rectangle(playerX, 550, 100, 8))) {
        ballYdir = -ballYdir;
      }
      A:
      for (int i = 0; i < mapGenerator.map.length; i++) {
        for (int j = 0; j < mapGenerator.map[0].length; j++) {
          if (mapGenerator.map[i][j] > 0) {
            int brickX = j * mapGenerator.brickWidth + 80;
            int brickY = i * mapGenerator.brickHeight + 50;
            int brickWidth = mapGenerator.brickWidth;
            int brickHeight = mapGenerator.brickHeight;

            Rectangle rectangle = new Rectangle(brickX, brickY, brickWidth, brickHeight);
            Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
            Rectangle brickRect = rectangle;
            if (ballRect.intersects(brickRect)) {
              mapGenerator.setBrickValue(0, i, j);
              totalBricks--;
              score += 5;

              if (ballposX + 19 <= brickRect.x || ballposX + i >= brickRect.x + brickRect.width) {
                ballXdir = -ballXdir;

              } else {
                ballYdir = -ballYdir;
              }
              break A;
            }
          }
        }
      }

      ballposX += ballXdir;
      ballposY += ballYdir;
      if (ballposX < 0) {
        ballXdir = -ballXdir;
      }
      if (ballposY < 0) {
        ballYdir = -ballYdir;
      }
      if (ballposX > 670) {
        ballXdir = -ballXdir;
      }
    }
    repaint();
  }

  @Override
  public void keyTyped(KeyEvent e) {}

  @Override
  public void keyReleased(KeyEvent e) {}

  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
      if (playerX >= 600) {
        playerX = 600;
      } else {
        moveRight();
      }
    }
    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
      if (playerX < 10) {
        playerX = 10;
      } else {
        moveLeft();
      }
    }
    if (e.getKeyCode() == KeyEvent.VK_ENTER){
      if (!play){
        play = true;
        ballposX = 120;
        ballposY = 350;
        ballXdir = -1;
        ballYdir = -2;
        playerX = 310;
        score = 0;
        totalBricks = 21;
        mapGenerator = new MapGenerator(3, 7);

        repaint();
      }
    }
  }

  public void moveRight() {
    play = true;
    playerX += 20;
  }

  public void moveLeft() {
    play = true;
    playerX -= 20;
  }
}
