import java.awt.geom.Point2D;
import java.util.Iterator;
import java.awt.Graphics;


public class World implements Dynamic {
	private ZombieCollection zc;
	private SurvivorCollection sc;
	private ResourceCollection rc;
	private Point2D.Double baseLoc;
	
	public World(double h, double w){
		zc = new ZombieCollection(h,w);
		zc.add(new Point2D.Double(50.0, 50.0));
		sc = new SurvivorCollection(h,w);
		rc = new ResourceCollection(h,w);
		baseLoc = new Point2D.Double(0.0, 0.0);
	}
	
	public Iterator<Point2D.Double> getZombies(){
		return zc.get();
	}
	
	public Iterator<Point2D.Double> getSurvivors(){
		return sc.get();
	}
	
	public Point2D.Double getBase(){
		return baseLoc;
	}
	
	public Iterator<Point2D.Double> getResources(){
		return rc.get();
	}
	
	public Iterator<Point2D.Double> getZombies(double xMin,double xMax, double yMin, double yMax){
		return zc.get(xMin, xMax, yMin, yMax);
	}
	
	public void update(int delay) {
		// TODO Auto-generated method stub
		sc.update(delay);
		zc.update(delay);
		
	}
	
	public void draw(Graphics g){
		zc.draw(g);
		sc.draw(g);
		rc.draw(g);
		
		
	}
}
