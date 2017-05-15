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

public class SpielFenster extends JFrame {
	private static final long serialVersionUID = 3327221473781403687L;
	public static final Logger logger = Logger.getAnonymousLogger();
	public static final int hoehe = getDefaultToolkit().getScreenSize().height / 2,
			breite = hoehe/* getDefaultToolkit().getScreenSize().width / 3 */, ticks = 250;
	private static SpielFenster instanz;
	private BufferedImage bImg;
	private Graphics2D graphics;
	private Timer timer;
	private int mausGedruecktX, mausGedruecktY, frame;

	private SpielFenster() {
		setSize(hoehe + 1, breite + 1);
		setResizable(false);
		setUndecorated(true);
		setPreferredSize(new Dimension(breite, hoehe));
		setMaximumSize(new Dimension(breite, hoehe));
		setMinimumSize(new Dimension(breite, hoehe));
		setLocation(new Point(getDefaultToolkit().getScreenSize().width / 2 - breite / 2,
				getDefaultToolkit().getScreenSize().height / 2 - hoehe / 2));
		listenerHinzufuegen();
		bImg = new BufferedImage(hoehe, breite, BufferedImage.TYPE_INT_RGB);
		graphics = (Graphics2D) bImg.getGraphics();
		timer = new Timer(ticks, (e) -> {
			aktualisieren();
			frame++;
		});
		logger.log(Level.CONFIG, "SpielFenster erstellt");

		setVisible(true);
	}

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

	private void aktualisieren() {
		logger.log(Level.FINEST, "SpielFenster aktualisiert [Frame " + frame + "]");
		Spiel.gibInstanz().zeichnen(graphics);
		zeichnen();
	}

	public void zeichnen() {
		if (isVisible())
			getGraphics().drawImage(bImg, 0, 0, hoehe, breite, null);
	}

	public void paintComponents(Graphics2D g) {
		super.paintComponents(g);
		g.drawImage(bImg, 0, 0, hoehe, breite, null);
		if (isVisible())
			getGraphics().drawImage(bImg, 0, 0, hoehe, breite, null);
	}

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

	public static SpielFenster gibInstanz() {
		if (instanz == null)
			instanz = new SpielFenster();
		return instanz;
	}

	public static void main(String[] args) {
		// System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF
		// %1$tT %4$s %2$s %5$s%6$s%n");
		// zeit, millisekunde, level, nachticht, neue zeile
		System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tT:%tL-%4$s: %5$s%6$s%n");
		logger.setLevel(Level.ALL);
		Handler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		logger.addHandler(handler);
		instanz = new SpielFenster();
		instanz.start();
	}
}
