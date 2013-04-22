package datatypes;


import java.util.ArrayList;
import java.awt.Point;


// CUSTOM CLASS
//		data structure that contains a sum of x coordinates, y coordinates,
//		and the number of coordinates stored from a root id
//		used to determine correct blob for led display and to determine center
public class Stats {
    
    // args
	private ArrayList<Point> coords;
	private int x_sum;
	private int y_sum;
	private int n;
    
    // CONSTRUCTOR METHODS
    public Stats() {
        coords = new ArrayList<Point>();
        x_sum = 0;
        y_sum = 0;
        n = 0;
    }
    
    // SET METHODS
    public void addCoord(int x, int y) {
        x_sum = x_sum + x;
        y_sum = y_sum + y;
        n = n + 1;
		Point in = new Point(x,y);
		coords.add(in);
    }
    
    // ACCESS METHODS
	// get x_sum
	public int getX() {
		return x_sum;
	}
	// get y_sum
	public int getY() {
		return y_sum;
	}
	// get n
	public int getN() {
		return n;
	}

	public ArrayList<Point> getCoords() {
		return coords;
	}
    
}
