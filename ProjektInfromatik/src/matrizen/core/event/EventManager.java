package matrizen.core.event;

import java.util.List;

public class EventManager {
	private static EventManager instanz;
	private List<EventListener<Event>> listeners;
	
	private EventManager() {
		listenerRegistrieren(new EventListener<BewegungsEvent>() {

			@Override
			public void ausfuehren(BewegungsEvent event) {
				
			}
			
		});
	}
	
	public static EventManager gibInstanz() {
		if(instanz == null)
			instanz = new EventManager();
		return instanz;
	}
	
	public void eventUebergeben(Event event) {
		for(EventListener<Event> l : listeners) {
			try {
				if(event.getClass().equals(l.getType()))
					l.ausfuehren(event);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void listenerRegistrieren(EventListener<? extends Event> l) {
		listeners.add((EventListener<Event>) l);
	}
}
