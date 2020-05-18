package misc;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import main.Panel;
import main.Program;

public class Mouse {
	
	private static Panel panel = Program.panel;
	private static MouseEvent lastEvent = null;
	private static boolean mouseDown = false;
	
	public static void init() {
		panel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				mouseDown = true;
				lastEvent = e;
			}
			
			public void mouseReleased(MouseEvent e) {
				mouseDown = false;
				lastEvent = e;
			}
		});
	}
	
	public static boolean isLeftButtonDown() {
		return (mouseDown && SwingUtilities.isLeftMouseButton(lastEvent));
	}
	
	public static boolean isRightButtonDown() {
		return (mouseDown && SwingUtilities.isRightMouseButton(lastEvent));
	}
	
	public static boolean isMiddleButtonDown() {
		return (mouseDown && SwingUtilities.isMiddleMouseButton(lastEvent));
	}
	
	public static int getX() {
		java.awt.Point pos = panel.getMousePosition();
		if (pos == null)
			return 0;
		return pos.x;
	}
	
	public static int getY() {
		java.awt.Point pos = panel.getMousePosition();
		if (pos == null)
			return 0;
		return pos.y;
	}
	
	public static int getXOnDisplay() {
		return (int)((double)getX()/panel.getWidth()*Program.DISPLAY_WIDTH);
	}
	
	public static int getYOnDisplay() {
		return (int)((double)getY()/panel.getHeight()*Program.DISPLAY_HEIGHT);
	}

	public static boolean mouseDown() {
		return mouseDown;
	}
	
	public static void reset() {
		mouseDown = false;
	}
}
