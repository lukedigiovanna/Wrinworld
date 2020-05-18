package quests;

public interface Questable {
	void init();
	boolean completed();
	void reward();
	String progress();
}
