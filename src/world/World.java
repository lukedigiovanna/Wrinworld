package world;

import java.awt.Point;
import java.io.*;
import java.util.*;

import javax.swing.JOptionPane;

import game.GameController;
import misc.Dimension;
import misc.MathUtils;
import misc.Position;
import world.gridComponent.GridComponent;

//just holds the collection of grid pieces that makes the world
//does not contain logic for entity interactions just stores the data of 
//grid component positions and perhaps data in them like a chest would have items.

public class World implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static void saveToFile(World world, String filePath) {
		try {
			FileOutputStream out = new FileOutputStream(filePath); 
			ObjectOutputStream oOut = new ObjectOutputStream(out);
			oOut.writeObject(world);
			oOut.close();
			JOptionPane.showMessageDialog(null, "Successfully saved world "+filePath, "Save Successful", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Unable to save world "+filePath, "Save Failure", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static World loadFromFile(String filePath) {
		//World world = null;
		try {
			FileInputStream fileIn = new FileInputStream(filePath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            World obj = (World)objectIn.readObject();
            objectIn.close();
            return obj;
		} catch (EOFException ex) {
			//try again
			ex.printStackTrace();
			return loadFromFile(filePath);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		//return world;
	}
	
	public static final int PIXELS_TO_METER = 16;
	public static final double DEFAULT_CHUNK_SIZE = 8;
	
	private double chunkSize = DEFAULT_CHUNK_SIZE;
	private double width, height;
	
	//the world is made of a 2d array of 'chunks' which are.. 
	private List<WorldObject>[][] chunks;
	
	public World() {
		this(30,30);
	}
	
	//centers world on its width.. eg. width = 300, -150 is 0 on the actual array.
	public World(double width, double height) {
		while (width % chunkSize != 0) width++;
		while (height % chunkSize != 0) height++;
		this.width = width;
		this.height = height;
		this.chunks = (ArrayList<WorldObject>[][]) new ArrayList[(int)(width/chunkSize)][(int)(height/chunkSize)];
		for (int x = 0; x < chunks.length; x++)
			for (int y = 0; y < chunks[x].length; y++)
				chunks[x][y] = new ArrayList<WorldObject>();
	}
	
	//gets the chunk that the coordinate is in
	public List<WorldObject> getChunk(double x, double y) {
		x+=(width/2);
		y+=(height/2);
		x/=chunkSize;
		y/=chunkSize;
		int fx = (int)x;
		int fy = (int)y;
		if (fx >= 0 && fx < chunks.length && fy >= 0 && fy < chunks[fx].length)
			return chunks[fx][fy];
		return null;
	}
	
	//gets the chunk at a specific x, y on the 2D array
	public List<WorldObject> getChunk(int x, int y) {
		if (x >= 0 && x < chunks.length && y >= 0 && y < chunks[x].length)
			return chunks[x][y];
		return null;
	}
	
	public boolean add(WorldObject o) {
		int chunkX = (int)(o.getX()/chunkSize+(width/2/chunkSize)), chunkY = (int)(o.getY()/chunkSize+(height/2/chunkSize));
		if (chunkX < 0 || chunkX >= chunks.length || chunkY < 0 || chunkY >= chunks[chunkX].length)
			return false;
		o.setChunkPosition(chunkX, chunkY);
		List<WorldObject> chunk = chunks[chunkX][chunkY];
		for (int i = 0; i < chunk.size(); i++) {
			if (o.compareTo(chunk.get(i)) == 0) { //remove it then break
				chunk.remove(i);
				break;
			}
		}
		chunk.add(o);
		return true;
	}
	
	//uses position/dimension in the gridcomponent scale. not the chunk scale
	//returns all world objects in a given position and dimension
	public List<WorldObject> get(double sx, double sy, double sWidth, double sHeight) {
		int bx = (int)(sx/chunkSize+width/2/chunkSize), by = (int)(sy/chunkSize+height/2/chunkSize);
		int bw = (int)(sWidth/chunkSize)+2, bh = (int)(sHeight/chunkSize)+2;
		return get(bx,by,bw,bh);
	}
	
	public List<WorldObject> get(Camera camera) {
		double sx = camera.position().x, sy = camera.position().y;
		double sw = camera.dimension().width, sh = camera.dimension().height;
		return get(sx,sy,sw,sh);
	}
	
	//returns all world objects in a chunk position/dimension
	public List<WorldObject> get(int sx, int sy, int sw, int sh) {
		List<WorldObject> list = new ArrayList<WorldObject>();
		for (int x = sx; x < sx+sw; x++)
			for (int y = sy; y < sy+sh; y++) {
				List<WorldObject> chunk = getChunk(x,y);
				if (chunk != null)
					for (int i = 0; i < chunk.size(); i++)  
						list.add(chunk.get(i));
			}
		return list;
	}
	
	public misc.Point getChunkPositionOfObject(WorldObject o) {
		for (int x = 0; x < chunks.length; x++)
			for (int y = 0; y < chunks[x].length; y++)
				if (chunks[x][y].contains(o))
					return new misc.Point(x/chunkSize-width/2,y/chunkSize-height/2);
		return null;
	}
	
	public List<WorldObject> getChunkOfObject(WorldObject o) {
		for (int x = 0; x < chunks.length; x++)
			for (int y = 0; y < chunks[x].length; y++)
				if (chunks[x][y].contains(o))
					return chunks[x][y];
		return null;
	}
	
	public GridComponent getGridComponent(double x, double y) {
		x = MathUtils.round(x, 0.5);
		y = MathUtils.round(y, 0.5);
		List<WorldObject> chunk = getChunk(x, y);
		for (WorldObject o : chunk) {
			if (o instanceof GridComponent && o.getX() == x && o.getY() == y) 
				return (GridComponent)o;
		}
		return null;
	}
	
	public double getChunkSize() {
		return this.chunkSize;
	}
	
	public int size() {
		int count = 0;
		for (int x = 0; x < chunks.length; x++)
			for (int y = 0; y < chunks[x].length; y++)
				if (chunks[x][y] != null)
					count+=chunks[x][y].size();
		return count;
	}
	
	//updates all world objects in view of a camera
	public void update(Position center, double radius) {
		List<WorldObject> objects = get(center.x-radius,center.y-radius,radius*2,radius*2);
		for (WorldObject o : objects) {
			if (o.world != this)
				o.world = this;
			o.update();
			o.correctChunk();
		}
	}
	
	public double width() {
		return width;
	}
	
	public double height() {
		return height;
	}
}
