import java.util.*;
import java.awt.geom.*;

public class PointCollection{
	private TreeSet<Point2D.Double> xSorted;
	private TreeSet<Point2D.Double> ySorted;
	
	private double worldHeight;
	private double worldWidth;
	
	
	public PointCollection(double h, double w){
		xSorted = new TreeSet<Point2D.Double>(new XPointCompare());
		ySorted = new TreeSet<Point2D.Double>(new YPointCompare());
		
		worldHeight = h;
		worldWidth = w;
	}
	
	public int size(){
		return xSorted.size();
	}
	
	public void setSets(TreeSet<Point2D.Double> set){
		xSorted.clear();
		ySorted.clear();
		xSorted.addAll(set);
		ySorted.addAll(set);
	}
	
	public double getWorldHeight(){
		return worldHeight;
	}
	
	public double getWorldWidth(){
		return worldWidth;
	}
	
	public void add(Point2D.Double point){
		xSorted.add(point);
		ySorted.add(point);
	}
	
	public void remove(Point2D.Double point){
		xSorted.remove(point);
		ySorted.remove(point);
	}
	
	//PROBLEM HERE
	public Iterator<Point2D.Double> get(double xMin, double xMax, double yMin, double yMax){
		Point2D.Double xMinPoint = new Point2D.Double(xMin, 0.0);
		Point2D.Double xMaxPoint = new Point2D.Double(xMax, 0.0);
		Point2D.Double yMinPoint = new Point2D.Double(0.0, yMin);
		Point2D.Double yMaxPoint = new Point2D.Double(0.0, yMax); 
		
		NavigableSet<Point2D.Double> xSet = xSorted.subSet(xMinPoint, true, xMaxPoint, true);
		NavigableSet<Point2D.Double> ySet = ySorted.subSet(yMinPoint, true, yMaxPoint, true);
		
		xSet.retainAll(ySet);
		
		return xSet.iterator(); 
	}
	
	public Iterator<Point2D.Double> get(){
		return xSorted.iterator();
	}
}

