import java.util.*;
import java.awt.geom.*;
import java.lang.Object;

public class XPointCompare implements Comparator<Point2D.Double> {

	public int compare(Point2D.Double o1, Point2D.Double o2){
		if (o1.x < o2.x)
			return -1;
		else if (o1.x == o2.x)
			return 0;
		else
			return 1;
	}
	
	public boolean equals(Object obj){
		return false;
	}
}
