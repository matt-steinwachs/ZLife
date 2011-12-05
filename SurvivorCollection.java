import java.util.*;
import java.awt.geom.*;
import java.awt.geom.Point2D.Double;
import java.awt.Graphics;
import java.awt.Color;



public class SurvivorCollection extends PointCollection implements Dynamic{
	private World world;
	private boolean isDelauney = false;
	private boolean isPotential = false;
	
	//Delaunay related variables;
	//private Map<N, Set<N>> theNeighbors; 
  //private Set<N> theNodeSet;
  private Point2D.Double currentSubTarget;
  private boolean subTargetReached = false;
  
  // Variables for determining current target for both methods
  // If you want to use them
  private Point2D.Double currentTarget;
  private boolean targetReached = false;
  private boolean targetIsResource = true;
	
	public SurvivorCollection(double h, double w, World wrld){
		super(h,w);
		world = wrld;
	}
	
	public void update(int delay){
		TreeSet<Point2D.Double> newPoints = new TreeSet<Point2D.Double>(new XPointCompare());
		
		Iterator<Point2D.Double> iter = get();
		while (iter.hasNext()){
			Point2D.Double p = iter.next();
			
			if (targetIsResource){
				Iterator<Point2D.Double> rIter = world.getResources();
				Point2D.Double rp = rIter.next();
				currentTarget = rp;
				
				if (rp.distance(p) < 20.0){
					targetReached = true;
					targetIsResource = false;
					currentTarget = world.getBase();
					System.out.println("targetReached");
				} else {
					targetReached = false;
				}
			} else {
				currentTarget = world.getBase();
				if (world.getBase().distance(p) < 20.0){
					targetReached = true;
					world.resourceGathered();
					targetIsResource = true;
					Iterator<Point2D.Double> resources = world.getResources();
					currentTarget = resources.next();
					
					System.out.println("targetReached");
				} else {
					targetReached = false;
				}
			}
			
			boolean dead = false;
			Iterator<Point2D.Double> zIter = world.getZombies();
			while (zIter.hasNext()){
				Point2D.Double zp = zIter.next();
				
				if (zp.distance(p) < 20.0){
					dead = true;
					targetReached = false;
					targetIsResource = true;
				}
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
		//if move mode is delaunay call delaunay(p) and return result
		if(isDelauney){
			return delaunay(p);
		}
		//else call potential(p) and return result
		else if(isPotential){
			return potential(p);
		}		
		return new Point2D.Double(0.0,0.0);
	}
	
	private Point2D.Double delaunay(Double p) {
		double xChange = currentTarget.x - p.x;
		double yChange = currentTarget.y - p.y;
				
		return new Point2D.Double(xChange, yChange);
		
		
//		Triangle tri =
//			new Triangle(new Pnt(0,super.getWorldHeight()*2), new Pnt(0,0), new Pnt(super.getWorldWidth()*2,0));
		//System.out.println("Triangle created: " + tri);
//		Triangulation dt = new Triangulation(tri);
		//System.out.println("DelaunayTriangulation created: " + dt);
//		Iterator<Point2D.Double> zombies = world.getZombies();
//		while (zombies.hasNext()){
//			Point2D.Double z = zombies.next();
//			dt.delaunayPlace(new Pnt(z.x,z.y));
//		}
//		//dt.delaunayPlace(new Pnt(0,0));
//		//dt.delaunayPlace(new Pnt(1,0));
//		//dt.delaunayPlace(new Pnt(0,1));
//		//System.out.println("After adding 3 points, we have a " + dt);
//		Triangle.moreInfo = true;
//		System.out.println("Triangles: " + dt.triGraph.nodeSet());
//		
//		return new Point2D.Double(1.0, 0.0);
	}

	private Point2D.Double potential(Double p) {
		Iterator<Point2D.Double> zombies = world.getZombies();
		Iterator<Point2D.Double> resources = world.getResources();
		Point2D.Double baseLoc = world.getBase();
		double xAdd, yAdd;
		if(p.x < resources.next().x){
			xAdd = 1.0;
		}
		else{
			xAdd = -1.0;
		}
		if(p.y < resources.next().y){
			yAdd = 1.0;
		}
		else{
			yAdd = -1.0;
		}
		return new Point2D.Double(p.x + xAdd, p.y + yAdd);
	}

	public void draw (Graphics g){
		g.setColor(new Color(0,0,255));
		
		Iterator<Point2D.Double> iter = this.get();
		while (iter.hasNext()){
			Point2D.Double p = iter.next();
			g.fillOval((int) p.x-10, (int) p.y-10, 20, 20);
		}
		
	} 
	public void setDelauney(){
		isDelauney = true;
		isPotential = false;
	}
	public void setPotential(){
		isDelauney = false;
		isPotential = true;
	}
}

