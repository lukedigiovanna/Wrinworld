package misc;

import java.io.Serializable;

public class Position implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public double x, y, priority = 0;
	
	public Position(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Position copy() {
		Position p = new Position(x,y);
		p.priority = this.priority;
		return p;
	}
	
	public boolean equals(Position other) {
		if (other == null)
			return false;
		return (this.x == other.x && this.y == other.y);
	}
}
