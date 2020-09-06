package entities.miscellaneous;

import java.awt.Image;

import animation.Animation;
import entities.*;
import main.Program;
import misc.Dimension;
import misc.MathUtils;
import misc.Vector2D;

public class Particle extends Entity {
	private static final long serialVersionUID = 1L;

	private Type type;
	private int lifeSpan;
	
	public Particle(Type type, double x, double y, double lifeSpan) {
		super(x,y,type.dimension.width,type.dimension.height);
		this.type = type;
		this.lifeSpan = (int)(lifeSpan*Program.TICKS_PER_SECOND);
		this.setInvulnerable(true);
		this.setTeam(Entity.Team.MISCELLANEOUS);
		this.showHealthBar = false;
	}
	
	private double ran = MathUtils.randomInRange(-1.0, 1.0);
	@Override
	public void individualUpdate() {
		type.update();
		
		//particle type specific stuff
		double xv, yv;
		switch (type) {
		case WHITE_SMOKE:
			//follow trig function
			yv = -1;
			xv = Math.cos(age/2.5+(ran*2*Math.PI));
			this.setVelocity(new Vector2D(xv,yv,Vector2D.FORM_BY_COMPONENTS));
			break;
		case BLOOD:
			yv = (age-10.0)/8.0;
			this.getVelocity().setYV(yv);
			//game.console().log(this.getVelocity().xv()+"");
			break;
		case HEART:
			yv=-1;
			this.getVelocity().setYV(yv);
			break;
		case FIRE:
			yv=-(1-(double)age/lifeSpan)*2.4;
			xv=Math.sin((double)age/lifeSpan*Math.PI*6+Math.random()*0.1);
			this.getVelocity().setYV(yv);
			this.getVelocity().setXV(xv);
			break;
		case MAGIC:
			yv=-1;
			this.getVelocity().setYV(yv);
			break;
		}
		
		if (this.age >= this.lifeSpan)
			this.destroy();
	}

	@Override
	public Entity replicate() {
		return new Particle(type,getX(),getY(),lifeSpan);
	}
	
	public Image entityImage() {
		return type.getImage();
	}
	
	public static enum Type {
		WHITE_SMOKE("whitesmoke","whitesmoke_",9,0.25,0.25),
		BLOOD("blood","blood_",10,0.25,0.25),
		HEART("heart.png",0.375,0.375),
		FIRE("fire","fireparticle_",6,5.0/16,5.0/16),
		MAGIC("magic","magicparticle_",6,5.0/16,5.0/16);
		
		Animation ani = null;
		Image img = null;
		
		Dimension dimension;
		
		String fp = "entities/particles/";
		
		Type(String aniPath, String prefix, int fps, double width, double height) {
			ani = new Animation(fp+aniPath,prefix,fps);
			ani.randomize();
			dimension = new Dimension(width,height);
		}
		
		Type(String imgPath, double width, double height) {
			img = Program.getImage(fp+imgPath);
			dimension = new Dimension(width,height);
		}
		
		public void update() {
			if (ani != null)
				ani.update();
		}
		
		public Image getImage() {
			if (img != null)
				return img;
			return ani.getCurrentFrame();
		}
	}
}
