import java.util.*;
import java.awt.geom.*;
import java.awt.Graphics;
import java.awt.Color;

public class SurvivorCollection extends PointCollection implements Dynamic{
	private World world;
	
	public SurvivorCollection(double h, double w, World wrld){
		super(h,w);
		world = wrld;
	}
	
	public void update(int delay){
		TreeSet<Point2D.Double> newPoints = new TreeSet<Point2D.Double>(new XPointCompare());
		
		Iterator<Point2D.Double> iter = get();
		while (iter.hasNext()){
			Point2D.Double p = iter.next();
			
			boolean dead = false;
			Iterator<Point2D.Double> zIter = world.getZombies();
			while (zIter.hasNext()){
				Point2D.Double zp = zIter.next();
				
				if (zp.distance(p) < 20.0)
					dead = true;
			}
			
			if(!dead){
				Point2D.Double move = nextMove(p);											//Treated as a vector
				double div = Math.sqrt(move.x*move.x + move.y*move.y);	//divisor to convert to unit vector
				newPoints.add(new Point2D.Double(p.x+move.x/div, p.y+move.y/div));
			}
		}
		
		setSets(newPoints);
	}
	
	public Point2D.Double nextMove(Point2D.Double p){
		//if move mode is delauney call delauney(p) and return result
		//else call potentialField(p) and return result
		return new Point2D.Double(1.0,0.0);
	}
	
	public void draw (Graphics g){
		g.setColor(new Color(0,0,255));
		
		Iterator<Point2D.Double> iter = this.get();
		while (iter.hasNext()){
			Point2D.Double p = iter.next();
			g.fillOval((int) p.x-10, (int) p.y-10, 20, 20);
		}
		
	} 
}

