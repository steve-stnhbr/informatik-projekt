package matrizen.model;

import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_CONTROL;
import static java.awt.event.KeyEvent.VK_D;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_E;
import static java.awt.event.KeyEvent.VK_ESCAPE;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_NUMPAD0;
import static java.awt.event.KeyEvent.VK_Q;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_S;
import static java.awt.event.KeyEvent.VK_SPACE;
import static java.awt.event.KeyEvent.VK_UP;
import static java.awt.event.KeyEvent.VK_W;
import static matrizen.view.SpielFenster.logger;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import matrizen.core.EingabeManager;
import matrizen.core.Konfiguration;
import matrizen.core.Richtung;
import matrizen.core.Vektor;
import matrizen.model.elemente.Gegner;
import matrizen.model.elemente.Item;
import matrizen.model.elemente.Spieler;
import matrizen.model.gegner.TestGegner;
import matrizen.view.SpielFenster;
import matrizen.view.hud.Text;

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
	private Text text;
	private boolean schiessen, gegnerErstellt;
	public boolean tutorial = true, schluesselAufheben, gegnerKannSterben;
	public boolean[] tutorials;
	public int tutorialTick;
	private static final int tutorialDelay = 35;

	private Spiel() {
		logger.log(java.util.logging.Level.INFO, "Spiel erstellt");
		Spieler.gibInstanz().setxFeld((int) Math.floor(spalten / 2));
		Spieler.gibInstanz().setyFeld((int) Math.floor(zeilen / 2));

		Level.level0.setNaechstesLevel(Level.level1);
		Level.level1.setNaechstesLevel(Level.level2);
		Level.level2.setNaechstesLevel(Level.level3);

		tutorials = new boolean[7];

		schiessen = !tutorial;
		level = Level.level1;
		if (!tutorial)
			SpielFenster.gibInstanz().addKeyListener(this);
	}

	static public Spiel gibInstanz() {
		if (instanz == null)
			instanz = new Spiel();
		return instanz;
	}

	public void zeichnen(Graphics2D graphics) {
		tutorialAbarbeiten();

		if (level.equals(Level.level1))
			tutorials[5] = true;

		level.zeichnen(graphics);
		Spieler.gibInstanz().zeichnen(graphics);
		if (text != null)
			text.zeichnen(graphics);
		ticks++;
	}

	/*
	 * private void tutorialAbarbeiten() { if (tutorial) { if (ticks == 0) {
	 * level = Level.level0; text = new
	 * Text("Bewege dich mit den WASD-Tasten oder Pfeil-",
	 * "tasten druch die Welt"); } if (ticks == 75)
	 * SpielFenster.gibInstanz().addKeyListener(this); if (ticks == 200) { text
	 * = new Text("Mit der Leertaste, E, Q oder STRG kannst du",
	 * "Partikel verschießen"); schiessen = true; }
	 * 
	 * if (ticks == 400) { text = new
	 * Text("Diese Partikel fügen den Gegnern Schaden zu,",
	 * "wenn du sie triffst"); } if (ticks == 600) { text = new
	 * Text("Gegner lassen Gegenstände Fallen,", "wenn du sie erledigst");
	 * level.hinzufuegen(new TestGegner(new Vektor(2, 3))); } if (ticks == 800)
	 * { text = new Text("Einer dieser Gegenstände ist dieser", "Schlüssel"); if
	 * (gegnerEntfernen()) level.hinzufuegen(new Item(Item.Typ.schluessel, new
	 * Vektor(7, 4))); } if (ticks == 1000) { text = new
	 * Text("Dieser Schlüssel erstellt ein Tele-", "Pad, wenn du ihn aufhebst");
	 * 
	 * } if (ticks == 1200) { text = new
	 * Text("Wenn du auf dieses Pad steigst, wirst",
	 * "du in das nächste Level teleportiert"); if (!level.getListe().isEmpty()
	 * && level.getListe().get(0) instanceof Item) { List<Levelelement> l =
	 * level.getListe(); if (l.size() > 0) l.remove(0); level.setListe(l);
	 * 
	 * level.setFeld(6, 3, Feld.Typ.WEITER); } } if (ticks == 1400) text = new
	 * Text("Die Herzen, die du hier siehst, regenerieren",
	 * "dein Leben wenn du sie aufhebst"); if (ticks == 1600) text = new
	 * Text("Alles andere, was du über das Spiel wissen",
	 * "musst, erfährst du im weiteren Verlauf"); if (ticks == 1800) { text =
	 * null; tutorial = false; } }
	 * 
	 * }
	 */

	private void tutorialAbarbeiten() {
		if (tutorial) {
			if (ticks == 0) {
				level = Level.level0;
				text = new Text("Bewege dich mit den WASD-Tasten oder Pfeil-", "tasten druch die Welt");
				SpielFenster.gibInstanz().addKeyListener(this);
			} else if (ticks > tutorialTick + tutorialDelay && tutorials[0] && !tutorials[1]) {
				text = new Text("Mit der Leertaste, E, Q oder STRG kannst du", "Partikel verschießen");
				schiessen = true;
			} else if (ticks > tutorialTick + tutorialDelay && tutorials[1] && !tutorials[2]) {
				text = new Text("Diese Partikel fügen den Gegnern Schaden zu,", "wenn du sie triffst");
				if (!gegnerErstellt) {
					level.hinzufuegen(new TestGegner(new Vektor(2, 3)));
					gegnerErstellt = true;
				}
			} else if (ticks > tutorialTick + tutorialDelay && tutorials[2] && !tutorials[3]) {
				text = new Text("Gegner lassen Gegenstände Fallen,", "wenn du sie erledigst");
				gegnerKannSterben = true;
			} else if (ticks > tutorialTick + tutorialDelay && ticks < tutorialTick + tutorialDelay * 1.75
					&& tutorials[3] && !tutorials[4]) {
				text = new Text("Einer dieser Gegenstände ist dieser", "Schlüssel");
			}
			if (ticks > tutorialTick + tutorialDelay * 1.75 && tutorials[3] && !tutorials[4]) {
				text = new Text("Dieser Schlüssel erstellt ein Tele-", "Pad, wenn du ihn aufhebst");
				schluesselAufheben = true;
			} else if (ticks > tutorialTick + tutorialDelay && tutorials[4] && !tutorials[5]) {
				text = new Text("Wenn du auf dieses Pad steigst, wirst", "du in das nächste Level teleportiert");
			} else if (ticks > tutorialTick + tutorialDelay && tutorials[5] && !tutorials[6])
				text = new Text("Die Herzen, die du hier siehst, regenerieren", "dein Leben wenn du sie aufhebst");
			else if (ticks > tutorialTick + tutorialDelay && tutorials[6])
				text = new Text("Alles andere, was du über das Spiel wissen", "musst, erfährst du im weiteren Verlauf");
			else if (ticks > tutorialTick + tutorialDelay * 1.5 && tutorials[6]) {
				text = null;
				tutorial = false;
			}
		}
	}

	private boolean gegnerEntfernen() {
		boolean ret = false;
		List<Levelelement> l = level.getListe();
		if (!l.isEmpty())
			for (Levelelement le : l)
				if (le instanceof Gegner) {
					l.remove(le);
					ret = true;
				}
		level.setListe(l);
		return ret;
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
			tutorials[0] = true;
			tutorialTick = (int) ticks;
			break;
		case VK_D:
		case VK_RIGHT:
			EingabeManager.aktivieren(Richtung.getIndex(Richtung.RECHTS));
			tutorials[0] = true;
			tutorialTick = (int) ticks;
			break;
		case VK_S:
		case VK_DOWN:
			EingabeManager.aktivieren(Richtung.getIndex(Richtung.UNTEN));
			tutorials[0] = true;
			tutorialTick = (int) ticks;
			break;
		case VK_A:
		case VK_LEFT:
			EingabeManager.aktivieren(Richtung.getIndex(Richtung.LINKS));
			tutorials[0] = true;
			tutorialTick = (int) ticks;
			break;
		case VK_SPACE:
		case VK_Q:
		case VK_E:
		case VK_CONTROL:
		case VK_NUMPAD0:
			if (schiessen) {
				EingabeManager.aktivieren(EingabeManager.gibEingaben().length - 1);
			}
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

	public void beenden() {

	}

	public void aendereLevel(Richtung links) {

	}
}
