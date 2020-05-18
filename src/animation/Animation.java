package animation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.util.List;

import main.Program;

import java.util.*;

//can be implemented in most other things.. all world objects will have one of these and the getImage function will return the current frame of the animation.

public class Animation implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//has frames that 
	private int timer = 0, index = 0;
	private transient List<BufferedImage> frames;
	private int fps = 1; //how many frames per second
	
	public Animation(List<BufferedImage> images, int fps) {
		frames = images;
		this.fps = fps;
	}
	
	public Animation(BufferedImage[] images, int fps) {
		frames = new ArrayList<BufferedImage>();
		for (BufferedImage img : images)
			frames.add(img);
		this.fps = fps;
	}
	
	public Animation(String folderName, String prefix, int fps) {
		int index = 0;
		List<BufferedImage> imgs = new ArrayList<BufferedImage>();
		String filePath = "library/assets/images/"+folderName+"/"+prefix+index+".png";
		File file = new File(filePath);
		while (file.exists()) {
			BufferedImage img = Program.getImage(filePath);
			imgs.add(img);
			index++;
			filePath = "library/assets/images/"+folderName+"/"+prefix+index+".png";
			file = new File(filePath);
		}
		frames = imgs;
		if (frames.size() == 0)
			frames.add(Program.getImage("RETURN THE TEXTURE NOT FOUND ERROR!"));
		this.fps = fps;
		randomize();
	}
	
	public void randomize() {
		index = (int)(Math.random()*frames.size());
		timer = (int)(Math.random()*(main.Program.TICKS_PER_SECOND/fps));
	}
	
	public BufferedImage getCurrentFrame() {
		return frames.get(index);
	}
	
	public void reset() {
		index = 0;
		timer = 0;
		looped = false;
	}
	
	//returns true if the animation has completed one loop
	//since it has last been resetted
	public boolean finished() {
		return looped;
	}
	
	private int loops = 0;
	private boolean looped = false;
	
	public void update() {
		//we assume that 1 second is equivalent to Program.TICKS_PER_SECOND updates (typically 20).
		//but this actual value is certainly slower in reality (more like we see 16 frames per second).
		//will still use Program.TICKS_PER_SECOND because instense lag is not expected
		int wait = main.Program.TICKS_PER_SECOND/fps;
		timer++;
		if (timer >= wait && frames.size() > 0) {
			//next frame
			if (index >= frames.size()-2) {
				loops++;
				looped = true;
			}
			index = (index+1)%frames.size();
			timer = 0;
		}
	}
}
