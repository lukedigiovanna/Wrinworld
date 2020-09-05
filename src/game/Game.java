package game;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import display.DisplayController;
import display.ImageProcessor;
import display.popup.PopupBar;

import java.awt.*;
import java.util.List;

import console.Console;

import java.util.*;
import java.awt.event.*;

import time.Date;
import main.*;
import misc.*;
import misc.Dimension;
import quests.MainQuestBook;
import quests.QuestBook;
import quests.QuestBookGUI;
import entities.*;
import entities.animals.*;
import entities.enemies.*;
import entities.miscellaneous.*;
import entities.npc.*;
import entities.player.*;
import entities.projectiles.*;
import items.Item;
import world.*;

public class Game implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//an instance of a certain game.. contains things like the player data, world data, progress etc.
	private EntityList entities;
	private Player player;
	private World world;
	private Camera camera;
	private Minimap minimap;
	private Date date;
	private GUIManager guiManager;
	private QuestBook questBook;
	
	private Console console = new Console();
	private boolean showQBGUI = false;
	private int age = 0;
	
	private QuestBookGUI qbGUI;
	
	private boolean initiated = false;
	
	private List<EventListener> listeners;
	
	private int portalCount;
	
	//basically the constructor.. isn't called, though, until the game actually starts and can be called again to reset.
	public void initiate() {
		Entity.setGame(this);
		
		world = World.loadFromFile("library/worlds/real.WORLD");
		entities = new EntityList(world);
		
		player = new Player(0,0);
		NPC npc = new Greeter(1,0);
		
		Spawner[] spawners = {
			new Spawner(0,0,world.width()/2,0.005,new Pig(0,0)),
			new Spawner(0,0,world.width()/2,0.005,new Cow(0,0)),
			new Spawner(0,0,world.width()/2-3,0.005,new Sheep(0,0)),
		};
		
		portalCount = (int)(world.width() * 0.6);
		SummoningPortal[] portals = new SummoningPortal[portalCount];
		Entity[] entityOrder = {new Slime(0,0), new FireDevil(0,0), new Minion(0,0), new Zombie(0,0), new Skeleton(0,0), new SwordZombie(0,0), new WildBoar(0,0), new Wizard(0,0), new Mage(0,0), new Giant(0,0)};
		double maxDistance = MathUtils.distance(0, 0, -world.width()/2+3, -world.height()/2+3);
		for (int i = 0; i < portalCount; i++) {
			//generate random x, y
			double x = MathUtils.round(MathUtils.randomInRange(-world.width()/2+3, world.width()/2-3), 0.5);
			double y = MathUtils.round(MathUtils.randomInRange(-world.height()/2+3, world.height()/2-3), 0.5);
			//find distance to center.. 
			double distance = MathUtils.distance(0, 0, x, y);
			while (distance < 3 || world.getGridComponent(x, y).hasCollision()) {
				x = MathUtils.round(MathUtils.randomInRange(-world.width()/2+3, world.width()/2-3), 0.5);
				y = MathUtils.round(MathUtils.randomInRange(-world.height()/2+3, world.height()/2-3), 0.5);
				//find distance to center.. 
				distance = MathUtils.distance(0, 0, x, y);
			}
			double percent = distance/maxDistance;
			//percent identifies difficulty.. 
			int mobs = MathUtils.randomInRange(1, 3);
			Entity[] ents = new Entity[mobs];
			double[] chances = new double[mobs];
			for (int j = 0; j < mobs; j++) {
				double offset = MathUtils.randomInRange(-percent/2, percent/2);
				double percentThroughList = percent+offset;
				int index = (int)(entityOrder.length*percentThroughList);
				index = MathUtils.clip(index, 0, entityOrder.length-1);
				Entity e = entityOrder[index].replicate();
				//if the offset is deeply negative then use more.. other way if it isnt
				double chance = 0.005-offset*0.1;
				chance = Math.abs(chance);
				chance/=4;
				chance/=mobs;
				chance = MathUtils.round(chance, 0.0001);
				ents[j] = e;
				chances[j] = chance;
			}
			double health = 1500*percent;
			SummoningPortal p = (new SummoningPortal(x,y,health)).setEntities(ents).setChances(chances);
			System.out.println(p);
			portals[i] = p;
		}
		
		NPC john = new John(11,-5);
	
		entities.add(player);
		for (Spawner spawner : spawners)
			entities.add(spawner);
		for (SummoningPortal p : portals) 
			entities.add(p);
		
		for (int i = 0; i < 12; i++) {
			Trader t = new Trader(Trader.Preset.values()[(int)(Math.random()*Trader.Preset.values().length)],MathUtils.randomInRange(-25, 25.0),MathUtils.randomInRange(-25.0, 25.0));
			entities.add(t);
		}
		
		camera = new Camera(new Position(-5,-4), new Dimension(10,8), player);
		
		minimap = new Minimap(this);
		
		date = new Date(
				60*60*7
				+60*60*24*150
				+60*12
				+10*60*60
			);
		
		//LISTENERS
		listeners = new ArrayList<EventListener>();
		listeners.add(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				lastKeyEvent = e;
				if (console.isCommandActive())
					return;
				switch (e.getKeyCode()) {
				case KeyEvent.VK_ESCAPE:
					DisplayController.setDisplay(DisplayController.PAUSE_DISPLAY);
					break;
				case KeyEvent.VK_T:
					GameController.game().date().setDay();
					break;
				case KeyEvent.VK_UP:
					GameController.game().camera().zoomIn();
					break;
				case KeyEvent.VK_DOWN:
					GameController.game().camera().zoomOut();
					break;
				case Controls.TALK_TO_NPC:
					GameController.game().entities().speakNPC();
					break;
				case Controls.TOGGLE_INVENTORY:
					if (guiManager.getCurrentID().equals("player_inventory"))
						guiManager.setGUI("none");
					else if (guiManager.getCurrentID().equals("none"))
						guiManager.setGUI("player_inventory");
					break;
				case KeyEvent.VK_R:
					showQBGUI = !showQBGUI;
					break;
				case KeyEvent.VK_LEFT:
					if (showQBGUI)
						qbGUI.previousPage();
					break;
				case KeyEvent.VK_RIGHT:
					if (showQBGUI)
						qbGUI.nextPage();
					break;
				case KeyEvent.VK_ENTER:
					if (showQBGUI) 
						qbGUI.initQuest();
					break;
				case KeyEvent.VK_F:
					console.togglePersist();
					break;
				case KeyEvent.VK_SLASH:
					console.openCommand();
					break;
				}
			}
			
			public void keyReleased(KeyEvent e) {
				lastKeyEvent = e;
			}
		});	
		
		listeners.add(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				mouseDown = true;
				lastMouseEvent = e;
			}
			
			public void mouseReleased(MouseEvent e) {
				mouseDown = false;
				lastMouseEvent = e;
			}
		});
		
		listeners.add(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				mouseDown = true;
				lastMouseEvent = e;
			}
		});
		
		listeners.add(player().getKey());
		listeners.add(player().getMouse());
		
		listeners.add(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				player().getInventory().setSelectedSlot(player().getInventory().getSelectedSlot()-e.getWheelRotation());
				if (showQBGUI) {
					if (e.getWheelRotation() < 0)
						qbGUI.nextPage();
					else
						qbGUI.previousPage();
					return;
				}
			}
		});
		
		questBook = new MainQuestBook(this);
		qbGUI = new QuestBookGUI(questBook);
		
		PlayerInventoryGUI piGUI = new PlayerInventoryGUI(this.player);
		
		guiManager = new GUIManager(piGUI, new TradeShopGUI());
	
		initiated = true;
	}
	
	public GUI getActiveGUI() {
		return this.guiManager.getCurrent();
	}
	
	public void setGUI(String id) {
		this.guiManager.setGUI(id);
	}
	
	private boolean mouseDown = false;
	public boolean isMouseDown() {
		return mouseDown;
	}
	
	public void setMouseDown(boolean t) {
		this.mouseDown = t;
	}
	
	private KeyEvent lastKeyEvent;
	public KeyEvent getKeyEvent() {
		return lastKeyEvent;
	}
	
	private MouseEvent lastMouseEvent;
	public MouseEvent getLastMouseEvent() {
		return lastMouseEvent;
	}
	
	public void resetPlayer() {
		player.reset();
	}
	
	//called by a thread responsible for game logic.. runs on the tick clock
	public void update() {
		if (!initiated)
			return;
		
		guiManager.update();
		PopupBar.updateCurrent();
		world.update(player.getPosition(),10.0);
		entities.update();
		date.addTime(60*15/Program.TICKS_PER_SECOND);
		questBook.update();
		console.update();
		
		ArrayList<WorldObject> objects = new ArrayList<WorldObject>();
		if (world != null)
			objects.addAll(world.get(this.camera));
		if (entities != null)
			objects.addAll(entities.get());

		for (int i = 0; i < objects.size(); i++)
			if (objects.get(i) == null) {
				objects.remove(i);
				i--;
			}
		
		//set all light values to be from the time of day
		for (WorldObject o : objects) 
			if (o != null)
				o.queueLightValue(date.lightvalue());
		//first update shit like light values
		ArrayList<LightSource> lightSources = new ArrayList<LightSource>();
		for (WorldObject l : objects) 
			if (l instanceof LightSource)
				for (WorldObject o : objects)
					if (o != l) {
						//find distance
						double distance = l.distance(o);
						double addtLight = ((LightSource)l).getPower()/(distance);
						o.queueLightValue(o.getQueuedLightValue()+addtLight);
					}
		
		for (WorldObject l : objects)
			l.updateLightValue();
		
		age++;
	}

	//called by a thread responsible for drawing
	//SHOULD NOT CONTAIN ANY LOGIC!
	private BufferedImage img = new BufferedImage(Program.DISPLAY_WIDTH,Program.DISPLAY_HEIGHT,BufferedImage.TYPE_INT_ARGB);
	private Graphics g = img.getGraphics();
	public Image getView() {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		
		if (!initiated) //dont even try to draw stuff
			return img;
		
		//draw the camera view of the world and entities
		if (initiated)
			g.drawImage(camera.get(world, entities), 0, 0, img.getWidth(), img.getHeight(), null);
		
		//draw the minimap
		int mx = 0, my = 0, mw = 150, mh = 150;
		g.drawImage(minimap.getImage(), mx+5, my+5, mw-5, mh-5, null);
		g.setColor(Color.WHITE);
		Graphics2.drawRect(g, mx+5, my+5, mw, mh, 5);
		
		//draw the date
		g.setColor(Color.LIGHT_GRAY);
		g.setFont(new Font("Arial",Font.ITALIC,16));
		String[] list = {
				date.toString(),
				//"X: "+MathUtils.round(player.getX(),0.1)+" Y: "+MathUtils.round(player.getY(),0.1),
				player.getHealth().toString(),
				player.getExperience().toString(),
				"$"+player.getMoney(),
				"Portals Left: "+entities.portalCount()+"/"+portalCount,
				"Lives Left: "+player.getLives()
				//"E: "+entities.get().size()
		};
		for (int i = 0; i < list.length; i++) 
			Graphics2.outlineText(g, list[i], 5, my+mh+22*(i+1), Color.BLACK);
		
		g.setFont(new Font("Arial",Font.PLAIN,14));
		Item i = player().getInventory().getSelectedItem();
		int x = 5, y = my+40+mh+22*(list.length+2);
		if (i != null) {
			Image icoImg = i.getIconImage();
			g.drawImage(i.getIconImage(), 5, my+mh+22*(list.length+1), 30, 30, null);
			Graphics2.outlineText(g, i.getName(), 5, my+30+mh+22*(list.length+2), Color.BLACK);	
			int w = 50, h = 8;
			if (i.getCooldownPeriod() > 0) {
				g.setColor(Color.BLACK);
				g.fillRect(x-2, y-2, w+4, h+4);
				g.setColor(Color.GRAY);
				g.fillRect(x, y, w, h);
				g.setColor(Color.WHITE);
				g.fillRect(x, y, (int)(w*i.cooldownStatusPercent()), h);
				g.setColor(Color.CYAN);
				g.fillRect(x, y, (int)(w*i.loadStatusPercent()), h);
			}
			//if the cooldown percent/load percent is between (0,1) draw it at the mouse
			w = 25; h = 4;
			int mx1 = Mouse.getXOnDisplay()-w/2;
			int my1 = Mouse.getYOnDisplay()+10;
			double cd = i.cooldownStatusPercent();
			double l = i.loadStatusPercent();
			if (cd > 0 && cd < 1) {
				g.setColor(Color.WHITE);
				g.fillRect(mx1, my1, (int)(w*cd), h);
			}
			if (l > 0 && l < 1) {
				g.setColor(Color.CYAN);
				g.fillRect(mx1, my1, (int)(w*l), h);
			}
			if (i.getMaxDurability() > 0) {
				y+=25;
				String s = "Durability: "+i.getDurability()+"/"+i.getMaxDurability();
				g.setColor(Color.WHITE);
				g.drawString(s, x, y);
			}
		}
		
		y+=25;
		g.setColor(Color.LIGHT_GRAY);
		Graphics2.outlineText(g, "Stamina: ", x, y, Color.BLACK);
		y+=10;
		//now draw the stamina bar
		int w = 50, h = 8;
		g.setColor(Color.BLACK);
		g.fillRect(x-2, y-2, w+4, h+4);
		g.setColor(Color.GRAY);
		g.fillRect(x, y, w, h);
		g.setColor(Color.CYAN);
		g.fillRect(x, y, (int)(w*player.staminaPercent()), h);
		
		//draw the mana bar.
		y+=25;
		g.setColor(Color.LIGHT_GRAY);
		Graphics2.outlineText(g, "Mana: ", x, y, Color.BLACK);
		y+=10;
		g.setColor(Color.BLACK);
		g.fillRect(x-2, y-2, w+4, h+4);
		g.setColor(Color.GRAY);
		g.fillRect(x, y, w, h);
		g.setColor(Color.BLUE);
		g.fillRect(x, y, (int)(w*player.getManaPercent()), h);
		
		if (!guiManager.getCurrentID().equals("player_inventory")) {
			BufferedImage hotbarImage = player.getInventory().getHotbarImage();
			int hotbarWidth = hotbarImage.getWidth();
			int hotbarHeight = hotbarImage.getHeight();
			double percentWidth = 0.6;
			int hotbarDrawnWidth = (int)(img.getWidth()*percentWidth);
			int hotbarDrawnHeight = (int)(((double)hotbarDrawnWidth/hotbarWidth)*hotbarHeight);
			g.drawImage(hotbarImage,img.getWidth()/2-hotbarDrawnWidth/2,img.getHeight()-hotbarDrawnHeight*2,hotbarDrawnWidth,hotbarDrawnHeight,null);
		}
		
		g.drawImage(console.getImage(), 0, img.getHeight()/2, img.getWidth()/2, img.getHeight()/2, null);
		
		g.drawImage(guiManager.getCurrentImage(), 0, 0, img.getWidth(), img.getHeight(), null);
		
		//draw any popup messages
		if (PopupBar.queue.size() > 0 && PopupBar.queue.get(0) != null) {
			BufferedImage image = PopupBar.queue.get(0).getImage();
			double sideMars = 0.03;
			g.drawImage(image, (int)(img.getWidth()*sideMars), img.getHeight()-image.getHeight()-(int)(img.getHeight()*sideMars), image.getWidth(), image.getHeight(), null);
		}		
		
		if (showQBGUI) {
			BufferedImage qbIMG = qbGUI.getImage();
			x = img.getWidth()/2-qbIMG.getWidth()/2; y = img.getHeight()/2-qbIMG.getHeight()/2;
			g.drawImage(qbIMG, x, y, qbIMG.getWidth(), qbIMG.getHeight(), null);
		}
		
		if (launchFW) {
			if (Program.FIREWORK_ANIMATION.finished())
				launchFW = false;
			
			Program.FIREWORK_ANIMATION.update();
			Image aniFrame = Program.FIREWORK_ANIMATION.getCurrentFrame();
			
			BufferedImage temp = new BufferedImage(29,25,BufferedImage.TYPE_INT_ARGB);
			temp.getGraphics().drawImage(aniFrame, 0, 0, temp.getWidth(), temp.getHeight(), null);
			temp = ImageProcessor.scaleToColor(temp, fwColor);
			
			g.drawImage(temp, 0, 0, img.getWidth(), img.getHeight(), null);
		}
		
		return img;
	}
	
	private Color[] loop = {Color.RED,Color.ORANGE,Color.YELLOW,Color.GREEN,Color.BLUE,Color.MAGENTA};
	private Color fwColor = Color.BLACK;
	private boolean launchFW = false;
	//puts a firework display atop the main game display
	public void launchFireworks() {
		launchFW = true;
		fwColor = loop[(int)(Math.random()*loop.length)];
		Program.FIREWORK_ANIMATION.reset();
	}
	
	public double angleToMouse() {
		MouseEvent e = lastMouseEvent;
		double xPercent = (double)e.getX()/Program.panel.getWidth(),
				yPercent = (double)e.getY()/Program.panel.getHeight();
		double xPos = xPercent*camera().dimension().width+camera().position().x;
		double yPos = yPercent*camera().dimension().height+camera().position().y;
		
		double radian = MathUtils.angle(this.player().getCenterX(), this.player().getCenterY(), xPos, yPos);
		return radian;
	}
	
	public void end() {
		
	}
	
	//GETTERS
	public Camera camera() {
		return camera;
	}
	
	public Player player() {
		return player;
	}
	
	public EntityList entities() {
		return entities;
	}
	
	public Date date() {
		return date;
	}
	
	public World getWorld() {
		return world;
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	public Console console() {
		return console;
	}
	
	public NPC getNPC(String name) {
		for (Entity e : entities.get()) {
			if (e instanceof NPC) {
				if (((NPC)e).getName().toLowerCase().equals(name.toLowerCase()))
					return (NPC)e;
			}
		}
		return null;
	}
	
	public List<EventListener> getListeners() {
		return listeners;
	}
}