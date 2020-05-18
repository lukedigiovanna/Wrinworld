package misc;

import java.io.Serializable;

public class Dimension implements Serializable {
	private static final long serialVersionUID = 2L;
	
	public double width, height;
	public Dimension(double width, double height) {
		this.width = width;
		this.height = height;
	}
	
	public double area() {
		return this.width*this.height;
	}
	
	public double perimeter() {
		return this.width*2+this.height*2;
	}
}
