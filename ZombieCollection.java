import java.util.*;
import java.awt.geom.*;
import java.awt.Color;
import java.util.Iterator;
import java.awt.Graphics;

public class ZombieCollection extends PointCollection implements Dynamic{
	private World world;
	
	public ZombieCollection(double h, double w, World wrld){
		super(h,w);
		world = wrld;
	}
	
	public void update(int delay){
//		TreeSet<Point2D.Double> newPoints = new TreeSet<Point2D.Double>(new XPointCompare());
//		
//		Iterator<Point2D.Double> iter = get();
//		while (iter.hasNext()){
//			Point2D.Double p = iter.next();
//			
//			newPoints.add(new Point2D.Double(p.x+1.0, p.y+1.0));
//		}
//		
//		setSets(newPoints);
	}
	
	public void draw (Graphics g){
		g.setColor(new Color(0,255,0));
		
		Iterator<Point2D.Double> iter = this.get();
		while (iter.hasNext()){
			Point2D.Double p = iter.next();
			g.fillOval((int) p.x-10, (int) p.y-10, 20, 20);
		}
	} 
}

