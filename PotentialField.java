import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.Iterator;

public class PotentialField {
	private World world;
	private Double target, survivor;
	private boolean targetResource;
	
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
			double zombieDist = survivor.distanceSq(zombie);
			double zombieThreat = 4000.0 / zombieDist;
			
			if(zombieThreat > 1.0){
				System.out.println(zombieThreat);
				Double runAway = new Point2D.Double(zombieDist * (survivor.x - zombie.x), zombieDist * (survivor.y - zombie.y));
				vec.setLocation(vec.x + runAway.x, vec.y + runAway.y);
			}
		}
		return vec;
	}

	private Double computeVector(Double element1, Double element2) {
		// TODO Auto-generated method stub
		double dist = element1.distance(element2);
		if(dist > 10.0)
			return new Point2D.Double(10 * (element2.x - element1.x), 10 * (element2.y - element1.y));
		else
			return new Point2D.Double(10 * (element2.x - element1.x), 10 * (element2.y - element1.y));
	}
}
