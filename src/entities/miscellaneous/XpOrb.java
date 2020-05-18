package entities.miscellaneous;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import display.ImageProcessor;
import entities.Entity;
import main.Program;
import misc.Graphics2;
import misc.Vector2D;
import sound.SoundManager;

public class XpOrb extends Entity {
	private int count = 0;
	
	public XpOrb(double x, double y, int size) {
		super(x,y,0.25,0.25);
		count = size;
		this.setInvulnerable(true);
		this.setTeam(Entity.Team.MISCELLANEOUS);
		showHealthBar = false;
	}
	

	@Override
	public void individualUpdate() {
		//crawl to player
		Entity follow = game.player();
		double radian = this.angle(follow);
		double speed = 0.5/this.distance(follow);
		if (this.distance(follow) > 3)
			speed = 0;
		this.setVelocity(new Vector2D(speed,radian,Vector2D.FORM_BY_RADIAN));
		//if we are colliding with the player.. then give them xp
		if (this.colliding(game.player())) {
			game.player().getExperience().addXP(count);
			SoundManager.playSound("xppickup");
			this.destroy();
		}
	}

	@Override
	public Entity replicate() {
		return new XpOrb(getX(),getY(),count);
	}

	private double percent = 0;
	private transient BufferedImage image = new BufferedImage(pixelWidth(),pixelHeight(),BufferedImage.TYPE_INT_ARGB);
	private transient Image orb = Program.getImage("library/assets/images/entities/misc/orb.png");
	private Graphics g = image.getGraphics();
	private Color[] loop = {Color.CYAN,Color.GREEN,Color.BLUE};
	@Override
	public Image entityImage() {
		percent+=Math.sin(age*0.2)*0.1;
		g.setColor(Graphics2.getColorInLoop(loop, percent));
		g.drawImage(orb,0,0,image.getWidth(),image.getHeight(),null);
		return ImageProcessor.brighten(ImageProcessor.scaleToColor(image, Graphics2.getColorInLoop(loop, percent)),0.5);
	}
}
