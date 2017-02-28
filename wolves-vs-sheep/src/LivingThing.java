import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class LivingThing implements Entity {
    Random rand = new Random();

    protected Point position;
    protected Pasture pasture;

    private final ImageIcon image = new ImageIcon("resources/icons/sheep.gif");

    public LivingThing(Pasture pasture) {
        this.pasture = pasture;
    }

    public LivingThing(Pasture pasture, Point position) {
        this.pasture   = pasture;
        this.position  = position;
    }

    public Point getPosition() { return position; }

    public void setPosition(Point newPosition) {
        if (newPosition.x >=0 && newPosition.x < this.pasture.getWidth() && newPosition.y >= 0 && newPosition.y < this.pasture.getHeight())
        position = newPosition; }

    public String type() {
        return "Sheep";
    }

    public void tick(){
        setPosition(new Point((int)getPosition().getX()+rand.nextInt(3)+(-1), (int)getPosition().getY()+rand.nextInt(3)+(-1)));
    }

    public ImageIcon getImage() {
        return image;
    }

}
