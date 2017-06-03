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
 * 
 * @author Stefan
 *
 */
public class Spiel implements KeyListener {
	public static final short zeilen = (short) 9, spalten = (short) 12;
	public static final float feldLaenge = SpielFenster.breite / Spiel.spalten;
	private static Spiel instanz;
	private Level level;
	private Konfiguration config;
	public long ticks;

	private Spiel() {
		logger.log(java.util.logging.Level.INFO, "Spiel erstellt");
		SpielFenster.gibInstanz().addKeyListener(this);
		level = Level.anfangsLevel;
		level.hinzufuegen(new TestGegner(new Vektor(2, 7)));
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
			EingabeManager.aktivieren(EingabeManager.gibEingaben().length - 1);
			break;
		}

		/*
		 * int c = e.getKeyCode();
		 * 
		 * if (c == KeyEvent.VK_ESCAPE) System.exit(0); else if (c ==
		 * config.getLinks())
		 * EingabeManager.aktivieren(Input.getIndex(Input.bewegungLinks); else
		 * if (c == config.getRechts())
		 * EingabeManager.aktivieren(Input.getIndex(Input.bewegungRechts); else
		 * if (c == config.getUnten())
		 * EingabeManager.aktivieren(Input.getIndex(Input.bewegungRunter); else
		 * if(c == config.getSchuss())
		 * EingabeManager.aktivieren(Input.getIndex(Input.schuss); else if(c ==
		 * config.getOben()) {
		 * EingabeManager.aktivieren(Input.getIndex(Input.bewegungHoch); }
		 */
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

	public enum Input {
		bewegungHoch, bewegungRunter, bewegungRechts, bewegungLinks, schuss;

		public static int getIndex(Input in) {
			for (int i = 0; i < values().length; i++)
				if (values()[i] == in)
					return i;
			return -1;
		}
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
			EingabeManager.deaktivieren(Input.getIndex(Input.bewegungHoch));
			break;
		case VK_D:
		case VK_RIGHT:
			EingabeManager.deaktivieren(Input.getIndex(Input.bewegungRechts));
			break;
		case VK_S:
		case VK_DOWN:
			EingabeManager.deaktivieren(Input.getIndex(Input.bewegungRunter));
			break;
		case VK_A:
		case VK_LEFT:
			EingabeManager.deaktivieren(Input.getIndex(Input.bewegungLinks));
			break;
		case VK_SPACE:
		case VK_Q:
		case VK_E:
		case VK_CLEAR:
		case VK_NUMPAD0:
			EingabeManager.deaktivieren(Input.getIndex(Input.schuss));
			break;
		}
	}
}
