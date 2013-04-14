package Away.DataTypes;

// CUSTOM CLASS
//		data structure that contains a sum of x coordinates, y coordinates,
//		and the number of coordinates stored from a root id
//		used to determine correct blob for led display and to determine center
public class Stats {
    
    // args
    private Point min_x;
    private Point max_x;
    private Point min_y;
    private Point max_y;
	private int x_sum;
	private int y_sum;
	private int n;
    
    // CONSTRUCTOR METHODS
    public Stats() {
        min_x = new Point(100000,100000);   // infinity
        max_x = new Point(0,0);             // zeros
        min_y = new Point(100000,100000);   // infinity
        max_y = new Point(0,0);
        x_sum = 0;
        y_sum = 0;
        n = 0;
    }
    
    // SET METHODS
    public void addCoord(int x, int y) {
        x_sum = x_sum + x;
        y_sum = y_sum + y;
        n = n + 1;
        if (x < min_x.getX()) {
            min_x.setLocation((double)x, (double)y);
        }
        if (x > max_x.getX()) {
            max_x.setLocation((double)x, (double)y);
        }
        if (y < min_y.getY()) {
            min_y.setLocation((double)x, (double)y);
        }
        if (y > max_y.getY()) {
            max_y.setLocation((double)x, (double)y);
        }
    }
    
    // ACCESS METHODS
	// get x_sum
	public int get_sumX() {
		return x_sum;
	}
	// get y_sum
	public int get_sumY() {
		return y_sum;
	}
	// get n
	public int get_count() {
		return n;
	}
    
}
