import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.Iterator;
import java.util.Random;
import java.awt.Graphics;
import java.awt.Color;


public class World implements Dynamic {
	private ZombieCollection zc;
	private SurvivorCollection sc;
	private ResourceCollection rc;
	private Point2D.Double baseLoc;
	
	private double height;
	private double width;
	
	private MyFrame myFrame;
	
	private Random rand;
	private double diameter;
	
	public World(double h, double w, MyFrame mf){
		height = h;
		width = w;
		myFrame = mf;
		rand = new Random();
		
		baseLoc = randomPointInWorld();
		
		if(mf.getApocalypse() == 10){
			diameter = 20;
		}
		else{
			diameter = 8;
		}
		
		zc = new ZombieCollection(h,w,this);
		if(mf.getApocalypse() == 10){
			this.addRandomZombies(10);
		}
		else{
			this.addRandomZombies(100);
		}
		
		sc = new SurvivorCollection(h,w,this);
		
		rc = new ResourceCollection(h,w,this);
		this.addRandomResources(1);
	}
	
	public void resourceGathered(){
		myFrame.resourceGathered();
	}
	
	public Iterator<Point2D.Double> getZombies(){
		return zc.get();
	}
	
	public Iterator<Point2D.Double> getZombies(double xMin,double xMax, double yMin, double yMax){
		return zc.get(xMin, xMax, yMin, yMax);
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
	
	public void addRandomZombies(int num){
		for (int i=0; i<num; i++){
			zc.add(randomPointInWorld());
		}
	}
	
	public void addRandomResources(int num){
		for (int i=0; i<num; i++){
			Point2D.Double candidate = randomPointInWorld();
			while (candidate.distance((Point2D) baseLoc) < 300.0)
				candidate = randomPointInWorld();
			rc.add(candidate);
		}
	}
	
	public void clearResources(){
		rc = new ResourceCollection(height,width,this);
	}
	
	public Point2D.Double randomPointInWorld(){
		return new Point2D.Double(rand.nextDouble()*width,
								rand.nextDouble()*height);
	}
	
	
	public void update(int delay) {
		if (sc.size() > 0)
			sc.update(delay);
		else
			sc.add(baseLoc);
		zc.update(delay);
	}
	
	public void draw(Graphics g){
		zc.draw(g);
		sc.draw(g);
		rc.draw(g);
		
		g.setColor(new Color(255,255,0));
		g.fillRect((int) baseLoc.x-10, (int) baseLoc.y-10, 20, 20);
	}
	
	public SurvivorCollection getSurvCollection(){
		return sc;
	}

	public double getDiameter() {
		// TODO Auto-generated method stub
		return diameter;
	}

	/*public void clearResource() {
		Iterator<Point2D.Double> iter2 = sc.get();
		Point2D.Double rc = new PotentialField(world, currentTarget, targetIsResource, iter2.next())).getBest();
		Iterator<Point2D.Double> iter = rc.get();
		while(iter.hasNext()){
			Point2D.Double point = iter.next();
			if(point.x == rp.x && point.y == rp.y){
				rc.remove(point);
			}
		}
	}*/
}
