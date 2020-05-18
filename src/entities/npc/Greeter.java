package entities.npc;

import java.awt.*;
import java.awt.image.BufferedImage;

import entities.Entity;
import entities.Path;
import main.Program;
import misc.Graphics2;
import misc.Vector2D;
import misc.Point;

public class Greeter extends NPC {
	
	public Greeter(double x, double y) {
		super(NPC.Character.GREETER,x, y);
		for (double radian = 0; radian < Math.PI*2; radian+=0.2) 
			path.add(new Point(x+Math.cos(radian)*0.5,y+Math.sin(radian)*0.5),0.6);
		this.setPath(path);
		this.setMaxSpeed(0.6);
	}
	
	Path path = (new Path(this)).loop();
	public void update() {
		super.update();
	}

	public BufferedImage entityImage() {
		int addt = (!hasSpoken()) ? 10 : 0;
		BufferedImage img = newImage(pixelWidth(),pixelHeight()+addt);
		Graphics g = img.getGraphics();
		g.drawImage(Program.getImage("player.png"), 0, addt, img.getWidth(), img.getHeight()-addt, null);
		g.setColor(Color.YELLOW);
		g.setFont(new Font("Arial",Font.BOLD,addt));
		if (!hasSpoken())
			g.drawString("!", Graphics2.centerX(g, "!",img.getWidth()), addt);
	
		return img;
	}

	@Override
	public void individualUpdate() {

	}
	
	public Entity replicate() {
		return new Greeter(position.x,position.y);
	}
}
