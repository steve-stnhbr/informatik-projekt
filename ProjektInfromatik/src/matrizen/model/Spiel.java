package matrizen.model;

import static java.awt.event.KeyEvent.*;

import java.awt.AWTEvent;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;

public class Spiel implements AWTEventListener {
	public static final short zeilen = (short) 15, spalten = (short) 15;
	private static Spiel instanz;
	private Level level;
	
	private Spiel() {
		Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK);
	}
	
	public static Spiel gibInstanz() {
		if(instanz == null)
			instanz = new Spiel();
		return instanz;
	}

	public void zeichnen(Graphics2D graphics) {
		level.zeichnen(graphics);		
	}

	@Override
	public void eventDispatched(AWTEvent event) {
		KeyEvent e = null;
		if(event instanceof KeyEvent)
			e = (KeyEvent) event;
		
		switch(e.getKeyCode()) {
		case VK_UP:
		case VK_W: input(Input.bewegungHoch);
			break;
		case VK_RIGHT:
		case VK_R: input(Input.bewegungRechts);
			break;
		case VK_LEFT:
		case VK_A: input(Input.bewegungLinks);
			break;
		case VK_DOWN:
		case VK_S: input(Input.bewegungRunter);
			break;
		case VK_SPACE:
		case VK_Q:
		case VK_E:
			input(Input.schuss);
			break;
		}
	}
	
	private void input(Input i) {
		
	}

	enum Input {
		bewegungHoch,
		bewegungRunter,
		bewegungRechts,
		bewegungLinks,
		schuss;
	}
}
