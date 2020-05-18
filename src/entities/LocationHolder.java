package entities;

import java.awt.Image;

//just holds a location.. no dimension or image
public class LocationHolder extends Entity {
	public LocationHolder(double x, double y) {
		super(x,y,new misc.Dimension(0,0));
	}
	
	@Override
	public Image entityImage() {
		return null;
	}

	@Override
	public void individualUpdate() {
		// TODO Auto-generated method stub
		
	}
	
	public Entity replicate() {
		return new LocationHolder(position.x,position.y);
	}
}
