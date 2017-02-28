import javax.swing.*;
import java.util.*;
import java.awt.*;

public class Dummy implements Entity {

    Random rand = new Random();

    private final ImageIcon image = new ImageIcon("resources/icons/unknown.gif");

    protected Point position;

    protected Pasture pasture;
   
    public Dummy(Pasture pasture) {
	    this.pasture = pasture;
    }

    public Dummy(Pasture pasture, Point position) {
	    this.pasture   = pasture;
	    this.position  = position;
    }
    
    public Point getPosition() { return position; }

    
    public void setPosition(Point newPosition) {
        if (newPosition.x >=0 && newPosition.x < this.pasture.getWidth() && newPosition.y >= 0 && newPosition.y < this.pasture.getHeight())
        position = newPosition; }

    
    public void tick() {

	    setPosition(new Point((int)getPosition().getX()+rand.nextInt(3)+(-1),(int)getPosition().getY()+rand.nextInt(3)+(-1)));

    }

    public String type() {
	return "Dummy";
    }
    
    public ImageIcon getImage() { return image; }

   
}
