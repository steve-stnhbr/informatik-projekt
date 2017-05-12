package matrizen.model;

import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_E;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_Q;
import static java.awt.event.KeyEvent.VK_R;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_S;
import static java.awt.event.KeyEvent.VK_SPACE;
import static java.awt.event.KeyEvent.VK_UP;
import static java.awt.event.KeyEvent.VK_W;

import java.awt.AWTEvent;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;

import matrizen.core.Konfiguration;
import matrizen.core.Richtung;
import matrizen.model.elemente.Spieler;

public class Spiel implements AWTEventListener {
	public static final short zeilen = (short) 15, spalten = (short) 15;
	private static Spiel instanz;
	private Level level;
	private Konfiguration config;

	private Spiel() {
		Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK);
		level = Level.anfangsLevel;
	}

	public static Spiel gibInstanz() {
		if (instanz == null)
			instanz = new Spiel();
		return instanz;
	}

	public void zeichnen(Graphics2D graphics) {
		level.zeichnen(graphics);
	}

	@Override
	public void eventDispatched(AWTEvent event) {
		KeyEvent e = null;
		if (event instanceof KeyEvent)
			e = (KeyEvent) event;
		
		int c = e.getKeyCode();
		
		if (c == KeyEvent.VK_ESCAPE)
			System.exit(0);
		else if (c == config.getLinks())
			input(Input.bewegungLinks);
		else if (c == config.getRechts())
			input(Input.bewegungRechts);
		else if (c == config.getUnten())
			input(Input.bewegungRunter);
		else if(c == config.getSchuss()) 
			input(Input.schuss);
		else if(c == config.getOben()) {
			input(Input.bewegungHoch);
		}
	}

	private void input(Input i) {
		switch(i) {
		case bewegungHoch:
			Spieler.gibInstanz().bewegen(Richtung.OBEN);
			break;
		case bewegungRechts:
			Spieler.gibInstanz().bewegen(Richtung.RECHTS);
			break;
		case bewegungRunter:
			Spieler.gibInstanz().bewegen(Richtung.UNTEN);
			break;
		case bewegungLinks:
			Spieler.gibInstanz().bewegen(Richtung.LINKS);
			break;
		}
	}

	enum Input {
		bewegungHoch, bewegungRunter, bewegungRechts, bewegungLinks, schuss;
	}
}
