package matrizen.view;

import static java.awt.Toolkit.getDefaultToolkit;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.logging.ConsoleHandler;
import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.Timer;

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
			breite = hoehe * Spiel.spalten / Spiel.zeilen, ticks = 50;
	private static SpielFenster instanz;
	private BufferedImage bImg;
	private Graphics2D graphics;
	private Timer timer;
	private int mausGedruecktX, mausGedruecktY, frame;

	private SpielFenster() {
		setSize(breite, hoehe);
		setResizable(false);
		setUndecorated(true);
		setPreferredSize(new Dimension(breite, hoehe));
		setMaximumSize(new Dimension(breite, hoehe));
		setMinimumSize(new Dimension(breite, hoehe));
		setLocation(new Point(getDefaultToolkit().getScreenSize().width / 2 - breite / 2,
				getDefaultToolkit().getScreenSize().height / 2 - hoehe / 2));
		listenerHinzufuegen();
		bImg = new BufferedImage(breite, hoehe, BufferedImage.TYPE_INT_RGB);
		graphics = (Graphics2D) bImg.getGraphics();
		timer = new Timer(ticks, (e) -> {
			aktualisieren();
			frame++;
		});
		logger.log(Level.CONFIG, "SpielFenster erstellt");

		setVisible(true);
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

				setLocation(getLocation().x + me.getX() - mausGedruecktX, getLocation().y + me.getY() - mausGedruecktY);
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
		logger.setFilter(new Filter() {
			@Override
			public boolean isLoggable(LogRecord record) {
				return true;
			}
		});
		logger.log(Level.CONFIG, "Spiel gestartet!");
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

	public static void main(String[] args) {
		// System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF
		// %1$tT %4$s %2$s %5$s%6$s%n");
		// zeit, millisekunde, level, nachticht, neue zeile
		// ändert die formatierung der Konsole, des logs
		System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tT:%tL-%4$s: %5$s%6$s%n");
		// ändert die wichtigkeit des loggers
		logger.setLevel(Level.ALL);
		Handler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		logger.addHandler(handler);
		// erstellt die instanz des SpielFensters
		instanz = new SpielFenster();
		instanz.start();
	}
}
