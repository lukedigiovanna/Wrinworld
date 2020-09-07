package entities;

import java.awt.Image;

//just holds a location.. no dimension or image
public class LocationHolder extends Entity {
	private static final long serialVersionUID = 1L;

	public LocationHolder(double x, double y) {
		super(x,y,new misc.Dimension(0,0));
	}
	
	@Override
	public Image entityImage() {
		return null;
	}

	@Override
	public void individualUpdate() {
		
	}
	
	public Entity replicate() {
		return new LocationHolder(position.x,position.y);
	}
}
