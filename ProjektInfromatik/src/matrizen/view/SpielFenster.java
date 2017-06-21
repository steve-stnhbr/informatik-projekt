package matrizen.view;

import static java.awt.Toolkit.getDefaultToolkit;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.Timer;

import matrizen.core.DateiManager;
import matrizen.core.MusikPlayer;
import matrizen.model.Spiel;

/**
 * Diese Klasse kümmert sich um die Darstellung des Spiels auf dem Bildschirm
 * 
 * @author Stefan
 *
 */
public class SpielFenster extends JFrame {
	private static final long serialVersionUID = 3327221473781403687L;
	public static final Logger logger = Logger.getAnonymousLogger();
	/**
	 * hier wird die höhe und die breite des Fensters festgelegt je nach dem, ob
	 * das fenster hoch oder querkant steht, wird immer von der längeren seite
	 * ausgehend die andere Seite berechnet
	 */
	public static final int hoehe = getDefaultToolkit().getScreenSize().height / 3 * 2,
			breite = hoehe * Spiel.spalten / Spiel.zeilen;
	private static SpielFenster instanz;
	private BufferedImage bImg;
	private Graphics2D graphics;
	private Timer timer;
	private int mausGedruecktX, mausGedruecktY, frame;

	private SpielFenster() {
		setSize(breite, hoehe);
		setResizable(false);
		setUndecorated(true);
		setIconImage(DateiManager.iconLaden());
		setPreferredSize(new Dimension(breite, hoehe));
		setMaximumSize(new Dimension(breite, hoehe));
		setMinimumSize(new Dimension(breite, hoehe));
		setLocation(new Point(getDefaultToolkit().getScreenSize().width / 2 - breite / 2,
				getDefaultToolkit().getScreenSize().height / 2 - hoehe / 2));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		listenerHinzufuegen();
		bImg = new BufferedImage((int) (Spiel.spalten * Spiel.feldLaenge), (int) (Spiel.zeilen * Spiel.feldLaenge),
				BufferedImage.TYPE_INT_RGB);
		graphics = (Graphics2D) bImg.getGraphics();
		timer = new Timer(DateiManager.werte.get("tick"), (e) -> {
			aktualisieren();
			frame++;
		});
		logger.log(Level.CONFIG, "SpielFenster erstellt");
		setVisible(false);
	}

	/**
	 * fügt Listener hinzu, die zulassen, dass das Fenster verschoben werden
	 * kann
	 */
	private void listenerHinzufuegen() {

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				mausGedruecktX = me.getX();
				mausGedruecktY = me.getY();
			}

			public void mouseDragged(MouseEvent me) {
				setLocation(getLocation().x + me.getX() - mausGedruecktX, getLocation().y + me.getY() - mausGedruecktY);
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent me) {
				Point nP = new Point(getLocation().x + me.getX() - mausGedruecktX,
						getLocation().y + me.getY() - mausGedruecktY);
				if (nP.x > 0 && nP.y > 0 && nP.x + breite < getDefaultToolkit().getScreenSize().width
						&& nP.y + hoehe < getDefaultToolkit().getScreenSize().height) {
					setLocation(nP);
					logger.log(Level.FINER, "Fenster an Position x=" + (getLocation().x + me.getX() - mausGedruecktX)
							+ ";y=" + (getLocation().y + me.getY() - mausGedruecktY));
				}
			}
		});
	}

	/**
	 * Methode, die jeden Tick ausgeführt wird und alle Elemente aktualisiert
	 * und zeichnet
	 */
	private void aktualisieren() {
		logger.log(Level.FINEST, "SpielFenster aktualisiert [Frame " + frame + "]");
		Spiel.gibInstanz().zeichnen(graphics);
		zeichnen();
	}

	/**
	 * zeichnet die Grafiken für jeden Tick
	 */
	public void zeichnen() {
		if (isVisible())
			getGraphics().drawImage(bImg, 0, 0, breite, hoehe, null);
	}

	/**
	 * Überschreibt die painComponents-Methode der Oberklasse, um die Grafik zu
	 * zeichnen
	 * 
	 * @param g
	 */
	public void paintComponents(Graphics2D g) {
		super.paintComponents(g);
		g.drawImage(bImg, 0, 0, breite, hoehe, null);
		if (isVisible())
			getGraphics().drawImage(bImg, 0, 0, breite, hoehe, null);
	}

	/**
	 * Startet den Timer und damit das Spiel
	 */
	public void start() {
		logger.log(Level.CONFIG, "Spiel gestartet!");
		Spiel.gibInstanz().tutorial = (DateiManager.config.getTutorial() == 0);
		timer.setDelay(DateiManager.werte.get("tick"));
		addKeyListener(Spiel.gibInstanz());
		setVisible(true);
		timer.start();
	}

	public void stop() {
		/*
		removeKeyListener(Spiel.gibInstanz());
		timer.stop();
		MusikPlayer.stop();
		setVisible(false);
		DateiManager.configSchreiben();
		DateiManager.werteSchreiben();
//		instanz = null;
		 */
	}

	public static void reset() {
		Spiel.reset();
	}

	public Timer getTimer() {
		return timer;
	}
	
	public void stopTimer() {
		timer.stop();
	}
	
	public void startTimer() {
		timer.start();
	}

	/**
	 * Singleton-Methode, die die statische Instanz des Spielfensters zurückgibt
	 * 
	 * @return die einzige Instanz der SpieleFenster-Klasse
	 */
	public static SpielFenster gibInstanz() {
		if (instanz == null)
			instanz = new SpielFenster();
		return instanz;
	}

	public static void init() {
		instanz = new SpielFenster();
	}
}
