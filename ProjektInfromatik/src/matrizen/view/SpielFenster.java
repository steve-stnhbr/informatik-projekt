package matrizen.view;

import static java.awt.Toolkit.getDefaultToolkit;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.Timer;

import matrizen.core.DateiManager;
import matrizen.core.MLogger;
import matrizen.model.Spiel;

public class SpielFenster extends JFrame {
	public static final MLogger l = new MLogger(Logger.getLogger(SpielFenster.class.getName()));
	public static final int hoehe = getDefaultToolkit().getScreenSize().height / 2,
			breite = hoehe/* getDefaultToolkit().getScreenSize().width / 3 */, ticks = 100;
	private static SpielFenster instanz;
	private BufferedImage bImg;
	private Graphics2D graphics;
	private Timer timer;

	private SpielFenster() {
		setSize(hoehe, breite);
		setResizable(false);
		setUndecorated(true);
		setPreferredSize(new Dimension(breite, hoehe));
		setMaximumSize(new Dimension(breite, hoehe));
		setMinimumSize(new Dimension(breite, hoehe));
		setLocation(new Point(0, 0));
		bImg = new BufferedImage(hoehe, breite, BufferedImage.TYPE_INT_RGB);
		graphics = (Graphics2D) bImg.getGraphics();
		timer = new Timer(ticks, (e) -> {
			aktualisieren();
		});
		l.setPrefix(MLogger.timePrefix);
		l.log(Level.FINE, "SpielFenster erstellt");

		setVisible(true);
	}

	private void aktualisieren() {
		l.log(Level.FINE, "SpielFenster aktualisiert [Frame " +ticks + "]");
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
		l.log(Level.INFO, "Spiel gestartet!");
		timer.start();
	}

	public static SpielFenster gibInstanz() {
		if (instanz == null)
			instanz = new SpielFenster();
		return instanz;
	}

	public static void main(String[] args) {
		instanz = new SpielFenster();
		instanz.start();
	}
}
