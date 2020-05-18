package mapMaking;

import javax.swing.JFrame;

import main.Program;

public class MapMakerDriver {
	public static void main(String[] args) {
		Program.initWithoutRunning();
		
		JFrame frame = new JFrame("Map Maker");
		frame.setSize(600,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new MapMakerPanel());
		frame.requestFocus();
		frame.setVisible(true);
	}
}