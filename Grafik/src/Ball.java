import java.awt.*;

public class Ball {

    private static final int D = 50; //diameter
    private int x = 0;
    private int y = 0;
    private int moveX = 3;
    private int moveY = 3;
    private int score = 0;
    Color ballColor = new Color(0xADFF2F);
    private Grafik grafik;

    public Ball(Grafik grafik){
        this.grafik = grafik;
    }

    private void moveBall(){
        if(x + moveX < 0)
            moveX = 3;
        if(x + moveX > grafik.getWidth() - D)
            moveX = -3;
        if(y + moveY < 0)
            moveY = 3;
        if(y + moveY > grafik.getHeight() - D)
            grafik.gameOver();
        if(collision()){
            moveY =  - 3;
            y = grafik.racket.getY() - D;
            setScore(1);
        }

        x += moveX;
        y += moveY;
    }

    public void move() {
        moveBall();
    }

    public void paint(Graphics2D g){
        g.setColor(ballColor);
        g.fillOval(x, y, D, D);
    }

    private Rectangle getBounds(){
        return new Rectangle(x, y, D, D);
    }

    private boolean collision(){

        return grafik.racket.getBounds().intersects(getBounds());

    }
    private void setScore(int score){
        this.score += score;
    }

    public int getScore(){
        return score;
    }

}