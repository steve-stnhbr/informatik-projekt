package matrizen.model.elemente;

import static matrizen.view.SpielFenster.logger;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.logging.Level;

import matrizen.core.Richtung;
import matrizen.core.Vektor;
import matrizen.core.Vektor.Rechenmethode;
import matrizen.core.event.BewegungsEvent;
import matrizen.core.event.EventManager;
import matrizen.model.Spiel;

/**
 * Diese Klasse repräsentiert den Spieler
 * 
 * @author Stefan
 *
 */
public class Spieler extends Figur {
	private static Spieler instanz;
	private Vektor posImFeld;
	private int index, delay = 5;
	private int[] cooldown;
	private Richtung blick;

	private Spieler() {
		posImFeld = Vektor.nullVektor;
		cooldown = new int[5];
	}

	public static Spieler gibInstanz() {
		if (instanz == null)
			instanz = new Spieler();
		return instanz;
	}

	@Override
	public void zeichnen(Graphics2D g) {
		/*
		int x = (int) (posImFeld.getX() * 32), y = (int) (posImFeld.getY() * 32);
		g.setStroke(new BasicStroke(1));
		g.setColor(Color.PINK);
		g.drawRect(x, y, (int) 32, (int) 32);
		*/

		if (ges.mag() < .5)
			g.drawImage(bildDrehen(grafik), (int) (posImFeld.getX() * 32),
					(int) (posImFeld.getY() * 32), (int) 32, (int) 32, null);
		else
			g.drawImage(bildDrehen(animation[index]), (int) (posImFeld.getX() * 32),
					(int) (posImFeld.getY() * 32), (int) 32, (int) 32, null);

		for (int i = 0; i < cooldown.length; i++) {
			int c = cooldown[i];
			if (c > 0)
				cooldown[i] = c - 1;
		}

		if (Spiel.gibInstanz().ticks % 5 == 0)
			index++;
	}

	private Image bildDrehen(BufferedImage grafik) {
		AffineTransform aT = AffineTransform.getRotateInstance(Math.toRadians(blick.winkel), grafik.getWidth(null) / 2, grafik.getHeight(null) / 2);
		AffineTransformOp operation = new AffineTransformOp(aT, AffineTransformOp.TYPE_BILINEAR);
		
		return operation.filter(grafik, null);
	}

	public void bewegen(Richtung r) {
		if (cooldown[Richtung.getIndex(r)] == 0 && bewegungMoeglich(r)) {
			EventManager.gibInstanz().eventUebergeben(new BewegungsEvent(posImFeld, posImFeld.add(r.vektor), r));
			pos = new Vektor(posImFeld.getX() * Spiel.feldLaenge, posImFeld.getY() * Spiel.feldLaenge);
			blick = r;
			cooldown[Richtung.getIndex(r)] = delay;
		}
	}

	/**
	 * Diese Methode überprüft, ob die Bewegung, die der Spieler machen mag,
	 * überhaupt möglich ist
	 * 
	 * @param r
	 * @return
	 */
	private boolean bewegungMoeglich(Richtung r) {
		Vektor v = posImFeld.add(r.vektor, Rechenmethode.kopieren);
		logger.log(Level.FINE, v.toString());
		return v.getX() >= 0 && v.getY() >= 0 && v.getX() < Spiel.spalten && v.getY() < Spiel.zeilen
				&& !Spiel.gibInstanz().getLevel().getFeld(v).isSolide();
	}

}
