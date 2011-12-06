import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.Iterator;

public class PotentialField {
	private World world;
	private Double target, survivor;
	private boolean targetResource;
	private double survivorDiameter = 8.0;
	
	public PotentialField(World wor, Double currentTarget,
			boolean targetIsResource, Double p) {
		// TODO Auto-generated constructor stub
		world = wor;
		target = currentTarget;
		targetResource = targetIsResource;
		survivor = p;
	}

	public Point2D.Double calculate() {
		// TODO Auto-generated method stub
//		System.out.println("resource:" + world.getResources().next().x + " " + world.getResources().next().y + 
//				"\ntarget:" + target.x + " " + target.y +
//				"\nbase:" + world.getBase().x + " " + world.getBase().y);
		
		// compute vector to target
		Double vec = this.computeVector(survivor, target);
		// compute vectors away from zombies
		for(Iterator<Double> zombies = world.getZombies();zombies.hasNext();){
			Double zombie = zombies.next();
			double zombieDist = survivor.distance(zombie) - survivorDiameter;
			double zombieThreat = 10.0 / zombieDist;
			
			if(zombieThreat > 0.0001){
				/*Double runAway = new Point2D.Double(0.3 * zombieDist * (survivor.x - zombie.x), 
						0.3 * zombieDist * (survivor.y - zombie.y));*/
				//System.out.println(zombieDist);
				Double runAway = new Point2D.Double(30 * zombieThreat * zombieThreat * (survivor.x - zombie.x), 
						30 * zombieThreat * zombieThreat * (survivor.y - zombie.y));
				if(runAway.x > runAway.y){
					runAway.x *= 2.0;
				}
				else{
					runAway.y *= 2.0;
				}
				vec.setLocation(vec.x + runAway.x, vec.y + runAway.y);
			}
		}
		return vec;
	}

	private Double computeVector(Double element1, Double element2) {
		// TODO Auto-generated method stub
		double dist = element1.distanceSq(element2);
		if(dist > 16000.0)
			return new Point2D.Double(10 * (element2.x - element1.x), 10 * (element2.y - element1.y));
		else 
			return new Point2D.Double(160000.0 / dist * (element2.x - element1.x), 160000.0 / dist * (element2.y - element1.y));
		/*if(dist > 10.0)
			return new Point2D.Double(10 * (element2.x - element1.x), 10 * (element2.y - element1.y));
		else
			return new Point2D.Double(30 * (element2.x - element1.x), 30 * (element2.y - element1.y));*/
	}
}
