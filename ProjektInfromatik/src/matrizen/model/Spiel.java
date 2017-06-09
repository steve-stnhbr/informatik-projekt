package matrizen.model;

import static java.awt.event.KeyEvent.getKeyText;
import static java.awt.event.KeyEvent.VK_ESCAPE;
import static matrizen.view.SpielFenster.logger;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import matrizen.core.DateiManager;
import matrizen.core.EingabeManager;
import matrizen.core.Konfiguration;
import matrizen.core.MusikPlayer;
import matrizen.core.Richtung;
import matrizen.core.Utils;
import matrizen.core.Vektor;
import matrizen.model.elemente.Spieler;
import matrizen.model.gegner.TestGegner;
import matrizen.view.SpielFenster;
import matrizen.view.hud.Text;
import matrizen.vorhinein.AnfangsFenster;
import matrizen.vorhinein.StartPanel;

/**
 * Dies ist die Hauptklasse, die auch den Input verwaltet
 */
public class Spiel implements KeyListener {
	public static final short zeilen = (short) 9, spalten = (short) 13;
	public static final float feldLaenge = SpielFenster.breite / Spiel.spalten;
	private static Spiel instanz;
	private Level level;
	public long ticks;
	private Text text;
	private boolean schiessen, gegnerErstellt, nichtZeichnen;
	public boolean tutorial = !DateiManager.config.isGespielt(), schluesselAufheben, gegnerKannSterben,
			kannTeleportieren;
	public boolean[] tutorials;
	public int tutorialTick;
	private static final int tutorialDelay = 100;

	private final int[] links = DateiManager.config.getLinks(), rechts = DateiManager.config.getRechts(),
			oben = DateiManager.config.getOben(), unten = DateiManager.config.getUnten(),
			schuss = DateiManager.config.getSchuss();

	private Spiel() {
		logger.log(java.util.logging.Level.INFO, "Spiel erstellt");
		Spieler.gibInstanz().setxFeld((int) Math.floor(spalten / 2));
		Spieler.gibInstanz().setyFeld((int) Math.floor(zeilen / 2));

		Level.level0.setNaechstesLevel(Level.level1);
		Level.level1.setNaechstesLevel(Level.level2);
		Level.level2.setNaechstesLevel(Level.level3);

		MusikPlayer.setZufall(true);
		MusikPlayer.setWiederholen(true);
		MusikPlayer.setWeiter(true);
		MusikPlayer.laden(DateiManager.Musik.alleLaden());
		MusikPlayer.naechstesLied();
		MusikPlayer.abspielen();

		tutorials = new boolean[8];

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

	private void tutorialAbarbeiten() {
		if (tutorial) {
			if (ticks == 0) {
				level = Level.level0;
				text = new Text(1, "Mit der esc-Taste kannst du das Spiel immer", "schließen");
			} else if (ticks == tutorialDelay) {
				text = new Text(0, "Mit den \"Bild\"-Tasten kannst du die ", "Lautstärke der Musik anpassen");
				SpielFenster.gibInstanz().addKeyListener(new KeyListener() {

					@Override
					public void keyTyped(KeyEvent e) {
						switch (e.getKeyCode()) {
						case KeyEvent.VK_PAGE_UP:
							MusikPlayer.setVolume(MusikPlayer.getVolume() + .025f);
							logger.log(java.util.logging.Level.FINE,
									"Lautstärke auf " + MusikPlayer.getVolume() + " gestellt");
							break;
						case KeyEvent.VK_PAGE_DOWN:
							MusikPlayer.setVolume(MusikPlayer.getVolume() - .025f);
							logger.log(java.util.logging.Level.FINE,
									"Lautstärke auf " + MusikPlayer.getVolume() + " gestellt");
							break;
						}
					}

					@Override
					public void keyReleased(KeyEvent e) {
					}

					@Override
					public void keyPressed(KeyEvent e) {
					}
				});
			} else if (ticks == tutorialDelay * 2) {
				// TODO
				text = new Text(0, "Bewege dich mit den WASD-Tasten oder Pfeil-", "tasten druch die Welt");
				SpielFenster.gibInstanz().addKeyListener(this);
			} else if (ticks > tutorialTick + tutorialDelay && tutorials[0] && !tutorials[1]) {
				text = new Text(0, "Mit und E und Strg kannst du", "Partikel verschießen");
				schiessen = true;
			} else if (ticks > tutorialTick + tutorialDelay && tutorials[1] && !tutorials[2]) {
				text = new Text(0, "Diese Partikel fügen den Gegnern Schaden zu,", "wenn du sie triffst");
				if (!gegnerErstellt) {
					level.hinzufuegen(new TestGegner(
							new Vektor(Utils.random(2, Spiel.spalten - 2), Utils.random(2, Spiel.zeilen - 2))));
					gegnerErstellt = true;
				}
			} else if (ticks > tutorialTick + tutorialDelay && tutorials[2] && !tutorials[3]) {
				text = new Text(0, "Gegner lassen Gegenstände Fallen, wenn du", "sie erledigst");
				gegnerKannSterben = true;
			} else if (ticks > tutorialTick + tutorialDelay / 1.5 && ticks < tutorialTick + tutorialDelay * 1.5
					&& tutorials[3] && !tutorials[4]) {
				text = new Text(0, "Einer dieser Gegenstände ist dieser", "Schlüssel");
			}
			if (ticks > tutorialTick + tutorialDelay * 1.5 && tutorials[3] && !tutorials[4]) {
				text = new Text(0, "Dieser Schlüssel erstellt ein Tele-", "Pad, wenn du ihn aufhebst");
				schluesselAufheben = true;
			} else if (ticks > tutorialTick + tutorialDelay && tutorials[4] && !tutorials[5]) {
				text = new Text(0, "Wenn du auf das Pad steigst, wirst du in", "das nächste Level teleportiert");
				kannTeleportieren = true;
			} else if (ticks > tutorialTick + tutorialDelay && tutorials[5] && !tutorials[6])
				text = new Text(0, "Die Herzen, die du hier siehst, regenerieren", "dein Leben, wenn du sie aufhebst");
			else if (ticks > tutorialTick + tutorialDelay * 2 && tutorials[6] && !tutorials[7]) {
				text = new Text(0, "                 Viel Spaß!");
				if (!tutorials[7])
					tutorialTick = (int) ticks;
				tutorials[7] = true;
			} else if (ticks > tutorialTick + tutorialDelay * 5 && tutorials[7]) {
				text = new Text(-1, "                 Viel Spaß!");
				tutorialTick = (int) ticks;
				tutorial = false;
			} else if (ticks > tutorialTick + 200 && tutorials[6]) {
				text = null;
				tutorialTick = 0;
				DateiManager.config.setGespielt(true);
			}
		}
	}

	/**
	 * Listener-Methode für Tastatureingaben
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int c = e.getKeyCode();
		if (c == oben[0] || c == oben[1]) {
			EingabeManager.aktivieren(Richtung.getIndex(Richtung.OBEN));
			if (!tutorials[0])
				tutorialTick = (int) ticks;
			tutorials[0] = true;
		} else if (c == rechts[0] || c == rechts[1]) {
			EingabeManager.aktivieren(Richtung.getIndex(Richtung.RECHTS));
			if (!tutorials[0])
				tutorialTick = (int) ticks;
			tutorials[0] = true;
		} else if (c == unten[0] || c == unten[1]) {
			EingabeManager.aktivieren(Richtung.getIndex(Richtung.UNTEN));
			if (!tutorials[0])
				tutorialTick = (int) ticks;
			tutorials[0] = true;
		} else if (c == links[0] || c == links[1]) {
			EingabeManager.aktivieren(Richtung.getIndex(Richtung.LINKS));
			if (!tutorials[0])
				tutorialTick = (int) ticks;
			tutorials[0] = true;
		} else if (c == schuss[0] || c == schuss[1]) {
			if (schiessen) {
				EingabeManager.aktivieren(EingabeManager.gibEingaben().length - 1);
			}
		} else if (c == KeyEvent.VK_PAGE_DOWN) {
			MusikPlayer.setVolume(MusikPlayer.getVolume() - .025f);
			logger.log(java.util.logging.Level.FINE, "Lautstärke auf " + MusikPlayer.getVolume() + " gestellt");
		} else if (c == KeyEvent.VK_PAGE_UP) {
			MusikPlayer.setVolume(MusikPlayer.getVolume() + .025f);
			logger.log(java.util.logging.Level.FINE, "Lautstärke auf " + MusikPlayer.getVolume() + " gestellt");
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int c = e.getKeyCode();
		if (c == VK_ESCAPE) {
			AnfangsFenster.gibInstanz().inhaltAendern(StartPanel.gibInstanz());
			SpielFenster.gibInstanz().stop();
		}
		else if (c == oben[0] || c == oben[1])
			EingabeManager.deaktivieren(Richtung.getIndex(Richtung.OBEN));
		else if (c == rechts[0] || c == rechts[1])
			EingabeManager.deaktivieren(Richtung.getIndex(Richtung.RECHTS));
		else if (c == unten[0] || c == unten[1])
			EingabeManager.deaktivieren(Richtung.getIndex(Richtung.UNTEN));
		else if (c == links[0] || c == links[1])
			EingabeManager.deaktivieren(Richtung.getIndex(Richtung.LINKS));
		else if (c == schuss[0] || c == schuss[1])
			EingabeManager.deaktivieren(4);
	}

	public void beenden() {

	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}
}
