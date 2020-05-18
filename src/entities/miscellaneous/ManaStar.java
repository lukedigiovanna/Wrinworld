package entities.miscellaneous;

import java.awt.Image;

import entities.Entity;
import main.Program;

public class ManaStar extends Entity {
	public ManaStar(double x, double y) {
		super(x,y,0.5,0.5);
	}

	@Override
	public void individualUpdate() {
		
	}

	@Override
	public Entity replicate() {
		return new ManaStar(getX(),getY());
	}

	private Image img = Program.getImage("entities/misc/manastar.png");
	@Override
	public Image entityImage() {
		return img;
	}
}
