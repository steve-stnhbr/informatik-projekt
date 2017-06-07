package matrizen.model;

import static java.awt.event.KeyEvent.*;
import static matrizen.view.SpielFenster.logger;

import java.awt.AWTEvent;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import matrizen.core.EingabeManager;
import matrizen.core.Konfiguration;
import matrizen.core.Richtung;
import matrizen.core.Vektor;
import matrizen.model.elemente.Geschoss;
import matrizen.model.elemente.Spieler;
import matrizen.model.gegner.TestGegner;
import matrizen.view.SpielFenster;

/**
 * Dies ist die Hauptklasse, die auch den Input verwaltet
 */
public class Spiel implements KeyListener {
	public static final short zeilen = (short) 9, spalten = (short) 13;
	public static final float feldLaenge = SpielFenster.breite / Spiel.spalten;
	private static Spiel instanz;
	private Level level;
	private Konfiguration config;
	public long ticks;

	private Spiel() {
		logger.log(java.util.logging.Level.INFO, "Spiel erstellt");
		SpielFenster.gibInstanz().addKeyListener(this);
		Spieler.gibInstanz().setxFeld((int) Math.floor(spalten / 2));
		Spieler.gibInstanz().setyFeld((int) Math.floor(zeilen / 2));
		level = Level.anfangsLevel;
	}

	static public Spiel gibInstanz() {
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
	 * Listener-Methode für Tastatureingaben
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case VK_ESCAPE:
			System.exit(0);
			break;
		case VK_W:
		case VK_UP:
			EingabeManager.aktivieren(Richtung.getIndex(Richtung.OBEN));
			break;
		case VK_D:
		case VK_RIGHT:
			EingabeManager.aktivieren(Richtung.getIndex(Richtung.RECHTS));
			break;
		case VK_S:
		case VK_DOWN:
			EingabeManager.aktivieren(Richtung.getIndex(Richtung.UNTEN));
			break;
		case VK_A:
		case VK_LEFT:
			EingabeManager.aktivieren(Richtung.getIndex(Richtung.LINKS));
			break;
		case VK_SPACE:
		case VK_Q:
		case VK_E:
		case VK_CONTROL:
		case VK_NUMPAD0:
			EingabeManager.aktivieren(EingabeManager.gibEingaben().length - 1);
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

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case VK_ESCAPE:
			System.exit(0);
			break;
		case VK_W:
		case VK_UP:
			EingabeManager.deaktivieren(Richtung.getIndex(Richtung.OBEN));
			break;
		case VK_D:
		case VK_RIGHT:
			EingabeManager.deaktivieren(Richtung.getIndex(Richtung.RECHTS));
			break;
		case VK_S:
		case VK_DOWN:
			EingabeManager.deaktivieren(Richtung.getIndex(Richtung.UNTEN));
			break;
		case VK_A:
		case VK_LEFT:
			EingabeManager.deaktivieren(Richtung.getIndex(Richtung.LINKS));
			break;
		case VK_SPACE:
		case VK_Q:
		case VK_E:
		case VK_CONTROL:
		case VK_NUMPAD0:
			EingabeManager.deaktivieren(4);
			break;
		}
	}
}
