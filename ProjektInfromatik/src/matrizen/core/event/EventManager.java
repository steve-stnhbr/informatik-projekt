package matrizen.core.event;

public class EventManager {
	private static EventManager instanz;
	
	private EventManager() {
		
	}
	
	public static EventManager gibInstanz() {
		if(instanz == null)
			instanz = new EventManager();
		return instanz;
	}
}
