import java.util.*;
import java.awt.geom.*;
import java.awt.Graphics;
import java.awt.Color;

public class ResourceCollection extends PointCollection implements Drawable{
	private World world;
	
	public ResourceCollection(double h, double w, World wrld){
		super(h,w);
		world = wrld;
	}
	
	public void update(int delay){
	
	
	}
	
	public void draw (Graphics g){
		g.setColor(new Color(255,0,0));
		
		Iterator<Point2D.Double> iter = this.get();
		while (iter.hasNext()){
			Point2D.Double p = iter.next();
			g.fillRect((int) p.x-10, (int) p.y-10, 20, 20);
		}
	} 
}

