package quests;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import main.Program;
import misc.Graphics2;

public class QuestBookGUI implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int width = (int)(Program.DISPLAY_WIDTH*0.4), height = (int)(Program.DISPLAY_HEIGHT*0.8);
	private transient BufferedImage img;
	private QuestBook qb;
	private int page = 0;
	
	public QuestBookGUI(QuestBook qb) {
		this.qb = qb;
		img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
	}
	
	public BufferedImage getImage() {
		Graphics g = img.getGraphics();
		g.setColor(new Color(255,160,20));
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial",Font.BOLD,28));
		g.drawString("Quest Book", width/2-g.getFontMetrics().stringWidth("Quest Book")/2, 40);
		//draw shit for the quest we are looking at
		Quest q = qb.getQuest(page);
		g.setColor(Color.DARK_GRAY);
		String s = q.getName();
		g.setFont(new Font("Arial",Font.ITALIC|Font.BOLD,16));
		int y = 70;
		g.drawString(s,5,y);
		g.setColor(Color.GRAY);
		g.setFont(new Font("Arial",Font.PLAIN,12));
		s = q.getDescription();
		y+=20;
		g.drawString(s, 7, y);
		
		y+=20;
		//list status now
		String status = qb.getQuest(page).getStatus();
		s = "Status: "+status;
		if (status.equals("Not Started"))
			g.setColor(Color.RED);
		else if (status.equals("Completed"))
			g.setColor(Color.GREEN);
		else {
			Color[] loop = {Color.LIGHT_GRAY,Color.GRAY,Color.LIGHT_GRAY};
			g.setColor(Graphics2.getColorInLoop(loop, qb.getPlayer().getAge()/20.0));
		}
		g.drawString(s, 5, y);
		
		g.setColor(Color.MAGENTA);
		String[] progress = qb.getQuest(page).getProgress().split("&");
		y+=25;
		for (String s1 : progress) {
			g.drawString(s1, 5, y);
			y+=20;
		}
		
		y+=25;
		//list the rewards now
		g.setColor(Color.BLACK);
		String[] rewards = qb.getQuest(page).getRewards().split("&");
		for (String s1 : rewards) {
			g.drawString(s1, 5, y);
			y+=20;
		}
		
		s = (page+1)+"";
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial",Font.BOLD,14));
		g.drawString(s, img.getWidth()-14, img.getHeight()-10);
		return img;
	}
	
	public void nextPage() {
		setPage(page+1);
	}
	
	public void previousPage() {
		setPage(page-1);
	}
	
	public void initQuest() {
		qb.activateQuest(page);
	}
	
	public void setPage(int page) {
		this.page = page;
		if (this.page < 0) this.page = 0;
		if (this.page > qb.length()-1) this.page = qb.length()-1;
	}
}
