package game;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import javax.swing.JOptionPane;

import display.DisplayController;
import display.GameDisplay;

//works with the current game that is loaded/being played
public class GameController {
	private static Game game = new Game();
	private static boolean initiated = false;
	
	public static void init() {
		if (!initiated) {
			game = new Game();
			game.initiate();
			//DisplayController.getDisplay(DisplayController.GAME_DISPLAY).activateListeners();
			System.out.println("initiated");
		}
		initiated = true;
	}
	
	public static void endGame() {
		DisplayController.getDisplay(DisplayController.GAME_DISPLAY).removeListeners();
		((GameDisplay)DisplayController.getDisplay(DisplayController.GAME_DISPLAY)).setAddedListeners(false);
		game = null;
		initiated = false;
	}
	
	private static String fp = ""; //nothing to start
	
	public static void setLoadFileName(String fp) {
		GameController.fp = fp;
	}
	
	public static Game getGameFromFile() {
		String filePath = "library/game-saves/"+fp+".GAME";
		return null;
	}
	
	public static void saveCurrentGame(String name) {
		if (true) return; //its not working right now so don't do it.
		String filePath = "library/game-saves/"+name+".GAME";
		try {
			FileOutputStream out = new FileOutputStream(filePath); 
			ObjectOutputStream oOut = new ObjectOutputStream(out);
			oOut.writeObject(game);
			oOut.close();
			JOptionPane.showMessageDialog(null, "Successfully saved world "+filePath, "Save Successful", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Unable to save world "+filePath, "Save Failure", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static Game game() {
		return game;
	}
	
	public static void updateCurrentGame() {
		if (game == null)
			return;
		
		//runs through the game logic
		game.update();
		
		if (game.player() != null && game.player().sendToDeathScreen())
			DisplayController.setDisplay(DisplayController.DEAD_DISPLAY);
	}
}
