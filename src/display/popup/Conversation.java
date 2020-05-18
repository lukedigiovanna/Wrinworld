package display.popup;

import java.io.Serializable;

public class Conversation implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String speakerName;
	private String[] messages;
	private int spot = 0;
	
	public Conversation(String name, String[] messages) {
		speakerName = name;
		this.messages = messages;
	}
	
	public PopupBar getNextBar() {
		if (spot >= messages.length)
			return null;
		PopupBar bar = new PopupBar(speakerName, messages[spot]);
		spot++;
		return bar;
	}
	
	public void reset() {
		spot = 0;
	}
	
	public boolean isSpoken() {
		if (spot >= messages.length)
			return true;
		return false;
	}
}
