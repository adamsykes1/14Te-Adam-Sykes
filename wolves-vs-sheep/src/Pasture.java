import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.MissingResourceException;
import java.util.Set;
import javax.swing.Timer;
import java.awt.event.*;
import java.awt.*;

public class Pasture implements ActionListener {

    /** A reference to use when setting the speed. */
    private final int  SPEED_REFERENCE = 1000;
    /** The entities that this pasture contains. */
    private Set        world = Collections.synchronizedSet(new HashSet());
    /** The speed of this simulation. */
    private int        speed           = 3;
    /** The timer that triggers ticks to be sent out to the entities. */
    private Timer      timer           = new Timer(SPEED_REFERENCE/speed,this);
    /** The width of this pasture */
    private int         width          = 25;
    /** The height of this pasture */
    private int         height         = 20;
   

    private PastureGUI gui;

    public Pasture(PastureGUI gui) {
        this.gui = gui;

        for (int i = 0; i < 10; i++) {
            try {
                Point position = new Point((int) (Math.random() * width),
                        (int) (Math.random() * height));

                Entity dummy = new Dummy(this, position);
                addEntity(dummy);

                Entity sheep = new LivingThing(this, position);
                addEntity(sheep);

                Entity plant = new Plant(this, position);
                addEntity(plant);
            } catch (MissingResourceException pe) {
                System.err.println("Pasture.initPasture(): " + pe.getMessage());
                System.exit(20);
            }
        }
    }

  
    public void actionPerformed(ActionEvent e) {
	Iterator it = getEntities().iterator();
	
	while(it.hasNext()) {
	    ((Entity)it.next()).tick();
	    gui.updateAll();
	}
	
    }

    
    public void addEntity(Entity entity) {
        synchronized(world) {
            world.add(entity);
        }
    }

    public Collection getEntities() {
	HashSet currentWorld = new HashSet();

        synchronized(world) {
	    Iterator it = world.iterator();

	    while(it.hasNext()) 
		currentWorld.add(it.next());
	}

	return currentWorld;
    }
        
   
    public void removeEntity(Entity entity) { 
        synchronized(world) {
            world.remove(entity);
        }
    }
       
    public void start() {
	
	timer.start();
    }
    
    public void stop() {
	timer.stop();
    }

    public int getWidth() {
	return width;
    }

    public int getHeight() {
	return height;
    }
}



