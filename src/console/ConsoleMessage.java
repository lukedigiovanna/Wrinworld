package console;

import main.Program;

public class ConsoleMessage {
	private String message;
	private int age;
	
	public ConsoleMessage(String message) {
		this.message = message;
	}
	
	public void update() {
		age++;
	}
	
	public int getAge() {
		return age;
	}
	
	public int getLifeSpan() {
		return Program.TICKS_PER_SECOND*5;
	}
	
	public String getMessage() {
		return message;
	}
}
