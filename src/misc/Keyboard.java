package misc;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import main.*;

import main.Program;

public class Keyboard {
	private static List<Integer> keys = new ArrayList<Integer>();
	private static List<KeyEvent> strokeHistory = new ArrayList<KeyEvent>();
	
	private static final int STROKE_HISTORY_SIZE = 1000;
	
	private static Panel panel = Program.panel;
	
	public static void init() {
		panel.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				keys.add(e.getKeyCode());
				strokeHistory.add(0,e);
				if (strokeHistory.size() > STROKE_HISTORY_SIZE) 
					strokeHistory.remove(STROKE_HISTORY_SIZE-1);
			}
			
			public void keyReleased(KeyEvent e) {
				for (int i = 0; i < keys.size(); i++)
					if (e.getKeyCode() == keys.get(i))
						keys.remove((int)i);
			}
		});
	}
	
	public static KeyEvent getKey(int position) {
		if (position > strokeHistory.size()-1) 
			return null;
		return strokeHistory.get(position);
	}
	
	public static boolean keyDown(int keycode) {
		return (keys.contains(keycode));
	}
	
	public static boolean anyKeyDown() {
		return (keys.size() > 0);
	}
	
	public static void reset() {
		keys.clear();
	}
}
