package matrizen.core.event;

import static matrizen.view.SpielFenster.logger;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * In dieser Klasse werden alle Events registriert, die für die Entwicklung notwendig sind.
 * Sie wird jedoch nur wirklich benötigt, wenn Puzzleelemente in das Proojekt eingefügt werden
 * @author Stefan
 *
 */
public class EventManager {
	private static EventManager instanz;
	private List<EventListener<Event>> listeners;
	
	private EventManager() {
		listeners = new ArrayList<>();
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
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void listenerRegistrieren(EventListener<? extends Event> l) {
		listeners.add((EventListener<Event>) l);
	}
}
