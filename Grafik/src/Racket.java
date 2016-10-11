/**
 * Created by adam.sykes on 2016-09-28.
 */
import java.awt.*;
import java.awt.event.KeyEvent;

public class Racket {

    private Grafik grafik;
    private int x = 350;
    private static final int Y =  550;
    private static final int WIDTH = 120;
    private static final int HEIGHT = 10;
    private int moveX = 0;

    public Racket(Grafik grafik) {
        this.grafik = grafik;
    }

    public void paint(Graphics2D g){
        g.setColor(Color.orange);
        g.fillRect(x, Y, WIDTH, HEIGHT);
    }

    void move(){
        if(x + moveX > 0 && x + moveX < grafik.getWidth() -  70)
            x += moveX;
    }

    public void keyPressed(KeyEvent e){

        if(e.getKeyCode() == KeyEvent.VK_LEFT)
            moveX = -2;
        if(e.getKeyCode() == KeyEvent.VK_RIGHT)
            moveX = 2;
    }
    public void keyReleased(KeyEvent e){
        moveX = 0;

    }

    public  Rectangle  getBounds(){
        return new  Rectangle(x, Y, WIDTH, HEIGHT);
    }

    public int getY(){
        return Y;
    }
}
