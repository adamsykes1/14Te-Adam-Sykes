import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;

public class Grafik extends JPanel {


    Ball ball = new Ball(this);
    Racket racket = new Racket(this);
    Font scoreFont = new Font("SansSerif", Font.PLAIN, 30);
    private int timeLeft;

    public Grafik(){
        addKeyListener(new KeyListener(){

            public void keyPressed(KeyEvent e){
                racket.keyPressed(e);
            }
            public void keyReleased(KeyEvent e){
                racket.keyReleased(e);
            }
            public void keyTyped(KeyEvent e){

            }
         });
        setFocusable(true);
    }

    private void move(){

        ball.move();
        racket.move();
    }

    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        ball.paint(g2d);
        setBackground(new Color(0xFFE4E1));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        racket.paint(g2d);
        g2d.setFont(scoreFont);
        g2d.drawString("Score: " + ball.getScore(), 15, 30);
        g2d.drawString("Spelet startar om: " + timeLeft, 420, 30);
    }

    public void gameOver(){
        JOptionPane.showMessageDialog(this, "Slutspelat", "SLUTSPELAT", JOptionPane.YES_NO_OPTION);
        System.exit(ABORT);
    }

    public void countDown(){
        try {
            for(timeLeft = 3; timeLeft > 0; timeLeft--){
                repaint();
                TimeUnit.SECONDS.sleep(1);
            }
        } catch(InterruptedException e){

        }
    }

    public static void main(String[] args){

       JFrame frame = new JFrame();
        Grafik grafik = new Grafik();
        frame.setSize(700, 600);
        frame.setLocation(300, 50);
        frame.add(grafik);
        frame.setTitle("Ett spel");
        frame.setResizable(false);
        frame.setVisible(true);

        grafik.countDown();

        while(true){
            grafik.move();
            grafik.repaint();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

}
