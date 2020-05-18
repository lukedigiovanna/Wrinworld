package console;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

import game.GameController;
import main.Program;
import misc.Keyboard;

public class Console implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ArrayList<ConsoleMessage> messages;
	private String command = "";
	
	public Console() {
		messages = new ArrayList<ConsoleMessage>();
	}
	
	public void openCommand() {
		command = "/";
	}
	
	public void closeCommand() {
		command = "";
	}
	
	public boolean isCommandActive() {
		return (!command.equals(""));
	}
	
	public void runCommand() {
		String s = command.substring(1);
		String[] tokens = s.split(" ");
		if (tokens.length == 0)
			return;
		switch(tokens[0]) {
		case "give":
			
			break;
		case "tp":
			if (tokens.length != 3)
				return;
			double x = Double.parseDouble(tokens[1]), y = Double.parseDouble(tokens[2]);
			GameController.game().player().setPosition(x,y);
			this.log("<c=y>Set player position to "+x+", "+y);
			break;
		case "summon":
			break;
		case "health":
			//set, add, remove
			String dir = (tokens.length >= 2) ? tokens[1] : "";
			double val = (tokens.length >= 3) ? Double.parseDouble(tokens[2]) : -123456789;
			if (dir.equals("")) {
				this.log("<c=r>Use health command: set, add, remove");
				break;
			}
			if (val == -123456789)
				this.log("<c=r>No value set");
			switch (dir) {
			case "set":
				GameController.game().player().setHealth(val);
				this.log("<c=y>Set player health to: "+val);
				break;
			case "add":
				GameController.game().player().heal(val);
				this.log("<c=y>Added "+val+" to player health");
				break;
			case "remove":
				GameController.game().player().hurt(val);
				this.log("<c=y> Removed "+val+" from player health");
				break;
			default:
				this.log("<c=r>Unknown health command: "+tokens[1]);
			}
			break;
		case "help":
			this.log("--COMMANDS--\n"+
					"give <item-id>\n"+
					"tp <x-coord> <y-coord>\n"+
					"summon <entity-id>");
			break;
		default:
			this.log("<c=r> Unknown command -- type help");
			break;
		}
		command = "";
	}
	
	public void update() {
		for (ConsoleMessage m : messages) 
			m.update();
		if (isCommandActive()) {
			KeyEvent front = Keyboard.getKey(0);
			if (front != lastTyped) {
				int keycode = front.getKeyCode();
				switch (keycode) {
				case KeyEvent.VK_SLASH:
					//don't type slashes
					break;
				case KeyEvent.VK_ESCAPE:
					this.closeCommand();
					break;
				case KeyEvent.VK_BACK_SPACE:
					command = command.substring(0,command.length()-1);
					break;
				case KeyEvent.VK_ENTER:
					runCommand();
					break;
				default:
					command+=front.getKeyChar();
				}
			}
			lastTyped = front;
		}
	}
	
	private KeyEvent lastTyped = null;
	
	public void log(String msg) {
		String[] msgs = msg.split("\n");
		for (String s : msgs)
			this.messages.add(new ConsoleMessage(s));
		while (this.messages.size() > 40)
			this.messages.remove(0);
	}
	
	private boolean persist = false;
	public void togglePersist() {
		persist = !persist;
	}
	
	private int timer = 0;
	public Image getImage() {
		timer++;
		BufferedImage image = new BufferedImage(Program.DISPLAY_WIDTH/2,Program.DISPLAY_HEIGHT/2,BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		g.setColor(new Color(125,125,125,125));
		if (persist)
			g.fillRect(0, 0, image.getWidth(), image.getHeight());
		int yChange = 12;
		String family = "Arial";
		int size = 14;
		g.setFont(new Font(family,Font.PLAIN,size));
		g.setColor(Color.WHITE);
		if (this.isCommandActive()) {
			String thing = (timer%10 < 5) ? "|" : "";
			g.drawString(this.command+thing, 3, image.getHeight()-yChange);
			yChange+=size+1;
		}
		for (int i = messages.size()-1; i >= 0; i--) {
			ConsoleMessage m = messages.get(i);
			if (m == null)
				continue;
			int x = 3;
			if (m.getAge() <= m.getLifeSpan() || persist) {
				//loop through each letter and look for commands
				String s = m.getMessage();
				for (int k = 0; k < s.length(); k++) {
					/*
					 * STYLE COMMANDS
					 * <c=(color)> (color) = r (red), b (blue), g (green), o (orange), p (pink), m (magenta), y (yellow)
					 * <s=(style)> (style) = p (plain), b (bold), i (italic), f (bold and italic)
					 */
					char c = s.charAt(k);
					//we know we need to be at five spaces from the end
					// <c=o>
					//012345 (size = 6, need to be at size-5)..
					//first letter must be '<', middle letter must be '=', and last letter must be '>'
					boolean cancel = false;
					if (c == '<' && k <= s.length()-5 && s.charAt(k+2) == '=' && s.charAt(k+4) == '>') { //parsing signal
						//begin parsing...
						char com = s.charAt(k+1);
						char val = s.charAt(k+3);
						switch (com) {
						case 'c': //color command
							switch (val) {
							case 'r':
								g.setColor(Color.RED);
								break;
							case 'o':
								g.setColor(Color.ORANGE);
								break;
							case 'y':
								g.setColor(Color.YELLOW);
								break;
							case 'g':
								g.setColor(Color.GREEN);
								break;
							case 'b':
								g.setColor(Color.BLUE);
								break;
							case 'm':
								g.setColor(Color.MAGENTA);
								break;
							case 'p':
								g.setColor(Color.PINK);
								break;
							case 'w':
								g.setColor(Color.WHITE);
								break;
							case 'l':
								g.setColor(Color.BLACK);
								break;
							case 't':
								g.setColor(Color.GRAY);
								break;
							case 'c':
								g.setColor(Color.CYAN);
								break;
							default:
								cancel = true; //no valid command
								break;
							}
							break;
						case 's': //style command
							switch (val) {
							case 'p':
								g.setFont(new Font(family,Font.PLAIN,size));
								break;
							case 'b':
								g.setFont(new Font(family,Font.BOLD,size));
								break;
							case 'i':
								g.setFont(new Font(family,Font.ITALIC,size));
								break;
							case 'f':
								g.setFont(new Font(family,Font.BOLD | Font.ITALIC,size));
								break;
							default:
								cancel = true;
							}
							break;
						default: //no valid style option
							cancel = true;
						}
						
						if (!cancel) {
							k+=4; //to next letter thats not the command
							continue; //go to next letter
						}
					}
					
					//add alpha component
					//fade out for 1.5 seconds (30 ticks)
					int ticksLeft = m.getLifeSpan()-m.getAge();
					Color col = g.getColor();
					if (ticksLeft <= 30 && ticksLeft >= 0) {
						int alpha = (int) (255*(ticksLeft/30.0));
						if  (persist)
							alpha = 255;
						g.setColor(new Color(col.getRed(),col.getGreen(),col.getBlue(),alpha));
					}
					g.drawString(c+"", x, image.getHeight()-yChange);
					x+=g.getFontMetrics().stringWidth(c+"");
				}
				yChange+=size+1;
			}
			if (yChange >= image.getHeight())
				break;
		}
		return image;
	}
}
