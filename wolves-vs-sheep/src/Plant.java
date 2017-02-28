import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * Created by adam.sykes on 2017-02-08.
 */
public class Plant implements Entity {
    Random rand = new Random();

    protected Point position;
    protected Pasture pasture;

    private final ImageIcon image = new ImageIcon("resources/icons/plant.gif");

    public Plant(Pasture pasture) {
        this.pasture = pasture;
    }

    public Plant(Pasture pasture, Point position) {
        this.pasture   = pasture;
        this.position  = position;
    }

    public Point getPosition() { return position; }

    public void setPosition(Point newPosition) {
        if (newPosition.x >=0 && newPosition.x < this.pasture.getWidth() && newPosition.y >= 0 && newPosition.y < this.pasture.getHeight())
            position = newPosition; }

    public String type() {
        return "Plant";
    }

    public void tick(){

    }

    public ImageIcon getImage() {
        return image;
    }
}
