package matrizen.view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.Timer;

import matrizen.core.Utils;
import matrizen.core.Vektor;
import matrizen.model.Feld;
import matrizen.model.Feld.Typ;
import matrizen.model.Spiel;

public class SpielFenster extends JFrame {
	public static final int hoehe = 600, breite = 600, ticks = 100;
	private static SpielFenster instanz;
	private BufferedImage bImg;
	private Graphics2D graphics;
	private Timer timer;

	private SpielFenster() {
		setSize(hoehe, breite);
		setResizable(false);
		setUndecorated(true);
		bImg = new BufferedImage(hoehe, breite, BufferedImage.TYPE_INT_RGB);
		graphics = (Graphics2D) bImg.getGraphics();
		timer = new Timer(ticks, (e) -> {
			aktualisieren();
		});
		
		setVisible(true);
	}

	private void aktualisieren() {
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
