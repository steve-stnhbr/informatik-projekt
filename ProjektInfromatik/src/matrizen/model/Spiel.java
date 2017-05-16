package matrizen.model;

import static java.awt.event.KeyEvent.*;
import static matrizen.view.SpielFenster.logger;

import java.awt.AWTEvent;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;

import matrizen.core.DateiManager;
import matrizen.core.Konfiguration;
import matrizen.core.Richtung;
import matrizen.model.elemente.Spieler;
import matrizen.view.SpielFenster;

/**
 * Dies ist die Hauptklasse, die auch den Input verwaltet
 * @author Stefan
 *
 */
public class Spiel implements AWTEventListener {
	public static final short zeilen = (short) 18, spalten = (short) 32;
	public static final float feldLaenge = SpielFenster.breite / Spiel.spalten;
	private static Spiel instanz;
	private Level level;
	private Konfiguration config;
	public long ticks;
	
	private Spiel() {
		logger.log(java.util.logging.Level.INFO, "Spiel erstellt");
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
		Spieler.gibInstanz().zeichnen(graphics);
		ticks++;
	}

	/**
	 * Listener-Methode f�r Tastatureingaben
	 */
	@Override
	public void eventDispatched(AWTEvent event) {
		KeyEvent e = null;
		if (event instanceof KeyEvent)
			e = (KeyEvent) event;
		
		switch(e.getKeyCode()) {
		case VK_ESCAPE:
			System.exit(0);
			break;
		case VK_W:
		case VK_UP:
			input(Input.bewegungHoch);
			break;
		case VK_D:
		case VK_RIGHT:
			input(Input.bewegungRechts);
			break;
		case VK_S:
		case VK_DOWN:
			input(Input.bewegungRunter);
			break;
		case VK_A:
		case VK_LEFT:
			input(Input.bewegungLinks);
			break;
		case VK_SPACE:
		case VK_Q:
		case VK_E:
			input(Input.schuss);
			break;
		}
		
		/*
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
		*/
	}

	/**
	 * hier wird der Input umgesetzt
	 * @param i
	 */
	private void input(Input i) {
		logger.log(java.util.logging.Level.FINE, "Input " + i + " registriert und ausgef�hrt");
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
		case schuss:
			break;
		default:
			break;
		}
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public Konfiguration getConfig() {
		return config;
	}

	public void setConfig(Konfiguration config) {
		this.config = config;
	}

	enum Input {
		bewegungHoch, bewegungRunter, bewegungRechts, bewegungLinks, schuss;
	}
}
