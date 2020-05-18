package misc;

import java.io.Serializable;

import main.Program;

public class Vector2D implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int FORM_BY_COMPONENTS = 0, FORM_BY_RADIAN = 1, FORM_BY_ANGLE = 2;
	//values are given as "meters"/second
	public double xc, yc, radian, magnitude;
	private double ax, ay;
	
	public Vector2D(double v1, double v2, int type) {
		switch (type) {
		case FORM_BY_RADIAN:
			magnitude = v1;
			radian = v2;
			xc = Math.cos(radian)*magnitude;
			yc = Math.sin(radian)*magnitude;
			break;	
		case FORM_BY_ANGLE:
			magnitude = v1;
			radian = MathUtils.getRadian(v2);
			xc = Math.cos(radian)*magnitude;
			yc = Math.sin(radian)*magnitude;
		default: 
			xc = v1;
			yc = v2;
			radian = MathUtils.angle(0, 0, xc, yc);
			radian%=(Math.PI*2);
			magnitude = Math.sqrt(xc*xc+yc*yc);
			break;
		}
	}	
	
	public Vector2D(Vector2D f) {
		this(f.xc,f.yc,FORM_BY_COMPONENTS);
	}
	
	public void setXV(double xv) {
		this.xc = xv;
		updateMagnitude();
	}
	
	public void setYV(double yv) {
		this.yc = yv;
		updateMagnitude();
	}
	
	public void setMagnitude(double m) {
		this.magnitude = m;
		updateComponents();
	}
	
	public void setDegree(double degree) {
		this.radian = MathUtils.getRadian(degree);
		updateComponents();
	}
	
	public void setRadian(double radian) {
		this.radian = radian;
		updateComponents();
	}
	
	public double getAngleDegree() {
		return MathUtils.getDegree(radian);
	}
	
	public void addXV(double toAdd) {
		xc+=toAdd;
		updateMagnitude();
	}
	
	public void addYV(double toAdd) {
		yc+=toAdd;
		updateMagnitude();
	}
	
	public void addDegree(double toAdd) {
		radian += MathUtils.getRadian(toAdd);
		updateComponents();
	}
	
	public void addRadian(double toAdd) {
		radian += toAdd;
		updateComponents();
	}
	
	public void addMagnitude(double toAdd) {
		magnitude+=toAdd;
		updateComponents();
	}
	
	public void updateMagnitude() {
		//uses the current xc and yc to update radian and magnitude
		radian = MathUtils.angle(0, 0, xc, yc);
		radian%=(Math.PI*2);
		magnitude = Math.sqrt(xc*xc+yc*yc);
	}
	
	public void updateComponents() {
		//uses the current magnit
		xc = Math.cos(radian)*magnitude;
		yc = Math.sin(radian)*magnitude;
	}
	
	public void setAcceleration(double val1, double val2, int type) {
		switch (type) {
		case Vector2D.FORM_BY_COMPONENTS:
			this.ax = val1;
			this.ay = val2;
			break;
		case Vector2D.FORM_BY_RADIAN:
			this.ax = Math.cos(val2)*val1;
			this.ay = Math.sin(val2)*val1;
			break;
		case Vector2D.FORM_BY_ANGLE:
			double radian = MathUtils.getRadian(val2);
			this.ax = Math.cos(radian)*val1;
			this.ay = Math.sin(radian)*val1;
			break;
		}
	}
	
	public void applyAcceleration() {
		this.setXV(this.xc-this.ax/Program.TICKS_PER_SECOND);
		this.setYV(this.yc-this.ay/Program.TICKS_PER_SECOND);
	}
	
	public void zero() {
		xc = 0; yc = 0; radian = 0; magnitude = 0;
	}
	
	public double xv() {
		return xc;
	}
	
	public double yv() {
		return yc;
	}
	
	public double getMagnitude() {
		return this.magnitude;
	}
	
	public double getRadian() {
		return this.radian;
	}
	
	public void add(Vector2D v) {
		//adding two vectors together
		this.xc+=v.xv();
		this.yc+=v.yv();
	}
}
