import java.util.*;
import java.awt.geom.*;
import java.awt.Color;
import java.util.Iterator;
import java.awt.Graphics;

public class ZombieCollection extends PointCollection implements Dynamic{
	
	public ZombieCollection(double h, double w){
		super(h,w);
	}
	
	public void update(int delay){
		TreeSet<Point2D.Double> newPoints = new TreeSet<Point2D.Double>();
		
		Iterator<Point2D.Double> iter = get();
		while (iter.hasNext()){
			Point2D.Double p = iter.next();
			
			newPoints.add(new Point2D.Double(p.x+1.0, p.y+1.0));
		}
		
		setSets(newPoints);
	}
	
	public void draw (Graphics g){
		g.setColor(new Color(0,255,0));
		
		Iterator<Point2D.Double> iter = this.get();
		while (iter.hasNext()){
			Point2D.Double p = iter.next();
			g.fillRect((int) p.x, (int) p.y, 20, 20);
		}
	} 
}

