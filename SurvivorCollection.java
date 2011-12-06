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
  private boolean subTargetReached = true;
  
  // Variables for determining current target for both methods
  private Point2D.Double currentTarget;
  private boolean targetReached = false;
  private boolean targetIsResource = true;
  
  //Change the size of survivors (should do the same for zombies)
  private double diameter = 8;
	
	public SurvivorCollection(double h, double w, World wrld){
		super(h,w);
		world = wrld;
	}
	
	public void update(int delay){
		TreeSet<Point2D.Double> newPoints = new TreeSet<Point2D.Double>(new XPointCompare());
		
		Iterator<Point2D.Double> iter = get();
		while (iter.hasNext()){
			Point2D.Double p = iter.next();
			
			
			//Has survivor reached target?
			if (targetIsResource){
				Iterator<Point2D.Double> rIter = world.getResources();
				Point2D.Double rp = rIter.next();
				currentTarget = rp;
				
				if (rp.distance(p) < diameter){
					targetReached = true;
					world.clearResources();
					world.addRandomResources(1);
					
					//set new target to base
					targetIsResource = false;
					currentTarget = world.getBase();
				} else {
					targetReached = false;
				}
			} else {
				currentTarget = world.getBase();
				if (world.getBase().distance(p) < diameter){
					targetReached = true;
					world.resourceGathered();
					targetIsResource = true;
					Iterator<Point2D.Double> resources = world.getResources();
					currentTarget = resources.next();
				} else {
					targetReached = false;
				}
			}
			
		
			
			boolean dead = false;
			Iterator<Point2D.Double> zIter = world.getZombies();
			while (zIter.hasNext()){
				Point2D.Double zp = zIter.next();
				
				if (zp.distance(p) < diameter){
					dead = true;
					targetReached = false;
					targetIsResource = true;
					subTargetReached = true;
					System.out.println("survivor died");
				}
			}
			
			if(!dead){
				Point2D.Double move = nextMove(p);											//Treated as a vector
				double div = Math.sqrt(move.x*move.x + move.y*move.y);	//divisor to convert to unit vector
				if (div != 0.0) {
					//System.out.println((p.x+move.x/div)+"..."+(p.y+move.y/div));
					newPoints.add(new Point2D.Double(p.x+move.x/div, p.y+move.y/div));
				} else {
					newPoints.add(new Point2D.Double(p.x, p.y));
					System.out.println("divide by zero avoided");
				}
			}
			
			//Has survivor reached subTarget?  Used for delaunay
			if (isDelauney && p.distance(currentSubTarget) < 10.0){
				subTargetReached = true;
				System.out.println("subtarget reached");
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
	
	private Point2D.Double delaunay(Point2D.Double p) {
		/*Straight Line Pathfinding for Testing*/
		//double xChange = currentTarget.x - p.x;
		//double yChange = currentTarget.y - p.y;
		//return new Point2D.Double(xChange, yChange);
		
		if (p.distance(currentTarget) < 100.0){
			currentSubTarget = currentTarget;
			System.out.println("Close enough to target. Going for it!.");
		} else if (subTargetReached){
			//Create delaunay triangulation
			Triangle initialTriangle =
				new Triangle(new Pnt(0,super.getWorldHeight()*2), 
											new Pnt(0,0), 
											new Pnt(super.getWorldWidth()*2,0));
			Triangulation dt = new Triangulation(initialTriangle);
			
			//Add zombies to triangulation
			Iterator<Point2D.Double> zombies = world.getZombies();
			while (zombies.hasNext()){
				Point2D.Double z = zombies.next();
				dt.delaunayPlace(new Pnt(z.x,z.y));
			}
			
			//Add resources to triangulation
			Iterator<Point2D.Double> resources = world.getResources();
			while (resources.hasNext()){
				Point2D.Double r = resources.next();
				dt.delaunayPlace(new Pnt(r.x,r.y));
			}
			
			//Add Base to Triangulation
			Point2D.Double base = world.getBase();
			dt.delaunayPlace(new Pnt(base.x,base.y));
			
			//Add points along border to triangulation
			double worldHeight = super.getWorldHeight();
			double worldWidth = super.getWorldWidth();
			dt.delaunayPlace(new Pnt(1, 1));
			dt.delaunayPlace(new Pnt(1, worldHeight/3));
			dt.delaunayPlace(new Pnt(1, worldHeight*2/3));
			dt.delaunayPlace(new Pnt(1, worldHeight-1));
			
			dt.delaunayPlace(new Pnt(worldWidth/3, 1));
			dt.delaunayPlace(new Pnt(worldWidth*2/3, 1));
			dt.delaunayPlace(new Pnt(worldWidth-1, 1));
			
			dt.delaunayPlace(new Pnt(worldWidth-1, worldHeight/3));
			dt.delaunayPlace(new Pnt(worldWidth-1, worldHeight*2/3));
			dt.delaunayPlace(new Pnt(worldWidth-1, worldHeight-1));
			
			dt.delaunayPlace(new Pnt(worldWidth/3, worldHeight-1));
			dt.delaunayPlace(new Pnt(worldWidth*2/3, worldHeight-1));

			//Add survivor location to triangulation
			//dt.delaunayPlace(new Pnt(p.x, p.y));
			
			//Add n random points to triangulation
			//int n = 0;
			//for (int j=0; j<n; j++){
			//	Point2D.Double rp = world.randomPointInWorld();
			//	dt.delaunayPlace(new Pnt(rp.x, rp.y));
			//}
			
			//get vertices of voronoi graph
			TreeSet<Point2D.Double> subTargets = new TreeSet<Point2D.Double>(new XPointCompare());
			int i = 0;
			HashSet<Pnt> done = new HashSet<Pnt>(initialTriangle);
				for (Triangle triangle : dt)
					for (Pnt site: triangle) {
						if (done.contains(site)) continue;
						done.add(site);
						List<Triangle> list = dt.surroundingTriangles(site, triangle);
						for (Triangle tri: list){
							Pnt cc = tri.getCircumcenter();
							subTargets.add(new Point2D.Double(cc.coord(0), cc.coord(1)));
						}
					}
			
			//Get closest vertex that is closer to the target
			Iterator<Point2D.Double> stIter =  subTargets.iterator();
			Point2D.Double nextSubTarget = new Point2D.Double();
			if (stIter.hasNext())
				nextSubTarget = stIter.next();
			else
				System.out.println("no candidate subtargets!");
			
			boolean suitableSubTargetFound = false;	
			while (stIter.hasNext()){
				Point2D.Double candidate = stIter.next();
				if (candidate.distance(p) < nextSubTarget.distance(p) &&
						candidate.distance(currentTarget) < p.distance(currentTarget) &&
						closestZombieToPath(p, candidate) > 30.0
						){
					nextSubTarget = candidate;
					suitableSubTargetFound = true;
				}
			}
			currentSubTarget = nextSubTarget;
			
			if (!suitableSubTargetFound){
				System.out.println("No suitable subtarget found.");
				currentSubTarget = currentTarget;
				subTargetReached = true;
			}
		}
		
		double xChange = currentSubTarget.x - p.x;
		double yChange = currentSubTarget.y - p.y;
		
		//System.out.println("new subtarget: ("+currentSubTarget.x+", "+currentSubTarget.y+")");
		return new Point2D.Double(xChange, yChange);
	}
	
	//returns the distance from a line (p1 to p2) to nearest zombie
	private double closestZombieToPath(Point2D.Double p1, Point2D.Double p2){
		Iterator<Point2D.Double> zombies = world.getZombies();
		double closest = 1000000000;
		while (zombies.hasNext()){
			Point2D.Double z = zombies.next();
			if (DistancePoint.distanceToSegment((Point2D) p1, (Point2D) p2, (Point2D) z) < closest)
				closest = DistancePoint.distanceToSegment(p1, p2, z);
		}
		return closest;
	}
	
	//returns the distance from a point to the closest zombie
	private double closestZombieDistance(Point2D.Double p){
		Iterator<Point2D.Double> zombies = world.getZombies();
		double closest = 1000000000;
		while (zombies.hasNext()){
			Point2D.Double z = zombies.next();
			if (p.distance(z) < closest)
				closest = p.distance(z);
		}
		return closest;
	}

	private Point2D.Double potential(Point2D.Double p) {
		return new PotentialField(world, currentTarget, targetIsResource, p).calculate();
	}

	public void draw (Graphics g){
		if (targetIsResource)
			g.setColor(new Color(0,0,255));
		else 
			g.setColor(new Color(255,0,255));
		
		Iterator<Point2D.Double> iter = this.get();
		while (iter.hasNext()){
			Point2D.Double p = iter.next();
			g.fillOval((int) (p.x-(diameter/2)), (int) (p.y-(diameter/2)), (int)diameter, (int)diameter);
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

