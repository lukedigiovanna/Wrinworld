package entities.player;

import java.awt.event.KeyEvent;

//static class to hold the controls for the game and player
public class Controls {
	public static final int 
		MOVE_UP = KeyEvent.VK_W,
		MOVE_RIGHT = KeyEvent.VK_D,
		MOVE_DOWN = KeyEvent.VK_S,
		MOVE_LEFT = KeyEvent.VK_A,
		TALK_TO_NPC = KeyEvent.VK_G,
		TALK_TO_TRADER = KeyEvent.VK_SPACE,
		TOGGLE_INVENTORY = KeyEvent.VK_E,
		THROW_ITEM = KeyEvent.VK_Q;
}
