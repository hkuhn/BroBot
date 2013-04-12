// CUSTOM CLASS
//		data structure that contains a sum of x coordinates, y coordinates,
//		and the number of coordinates stored from a root id
//		used to determine correct blob for led display and to determine center
public class Stats {
    // fields
	int x_sum;
	int y_sum;
	int n;
    // methods
	// constructor
	public Stats(int x, int y, int num) {
		x_sum = x;
		y_sum = y;
		n = num;
	}
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
}