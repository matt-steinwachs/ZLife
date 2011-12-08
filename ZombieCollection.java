import java.util.*;
import java.awt.geom.*;
import java.awt.Color;
import java.util.Iterator;
import java.awt.Graphics;

public class ZombieCollection extends PointCollection implements Dynamic{
	private World world;
	private int diameter = 20;
	
	public ZombieCollection(double h, double w, World wrld){
		super(h,w);
		world = wrld;
	}
	
	public void update(int delay){
		TreeSet<Point2D.Double> newPoints = new TreeSet<Point2D.Double>(new XPointCompare());
		
		Iterator<Point2D.Double> iter = get();
		while (iter.hasNext()){
			Point2D.Double p = iter.next();
			
			Point2D.Double target = new Point2D.Double();
			boolean alerted = false;
			Iterator<Point2D.Double> sIter = world.getSurvivors();
			while (sIter.hasNext()){
				Point2D.Double sp = sIter.next();
				
				if (sp.distance(p) < 250.0 && p.distance(target) > p.distance(sp)){
					target = sp;
					alerted = true;
				}
			}
			
			boolean destroyed = false;
			if (world.getBase().distance(p) < 30.0)
				destroyed = true;
			
			if (alerted && !destroyed){
				double xChange = target.x - p.x;
				double yChange = target.y - p.y;
				double div = Math.sqrt(xChange*xChange + yChange*yChange);
				
				newPoints.add(new Point2D.Double(p.x+xChange/(div*2.0), p.y+yChange/(div*2.0)));
			} else if (!destroyed) {
				newPoints.add(new Point2D.Double(p.x, p.y));
			} else {
				newPoints.add(world.randomPointInWorld());
			}
		}
		
		setSets(newPoints);
	}
	
	public void draw (Graphics g){
		g.setColor(new Color(0,255,0));
		
		Iterator<Point2D.Double> iter = this.get();
		while (iter.hasNext()){
			Point2D.Double p = iter.next();
			g.fillOval((int) p.x-(diameter/2), (int) p.y-(diameter/2), diameter, diameter);
		}
	} 
}

