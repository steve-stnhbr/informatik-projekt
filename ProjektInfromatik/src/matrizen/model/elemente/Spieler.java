package matrizen.model.elemente;

import static matrizen.view.SpielFenster.logger;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageFilter;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.util.logging.Level;

import matrizen.core.DateiManager;
import matrizen.core.EingabeManager;
import matrizen.core.Richtung;
import matrizen.core.Vektor;
import matrizen.core.Vektor.Rechenmethode;
import matrizen.core.event.BewegungsEvent;
import matrizen.core.event.EventManager;
import matrizen.model.Spiel;
import matrizen.view.SpielFenster;

/**
 * Diese Klasse repr�sentiert den Spieler
 * 
 * @author Stefan
 *
 */
public class Spieler extends Figur {
	private static Spieler instanz;
	private Vektor posImFeld;
	private int index;
	short delay = 5;
	private short[] cooldown;
	private Richtung blick = Richtung.OBEN;

	private Spieler() {
		cooldown = new short[5];
		grafik = DateiManager.laden(DateiManager.Bild.elementSpieler);
		posImFeld = Vektor.nullVektor;
	}

	public static Spieler gibInstanz() {
		if (instanz == null)
			instanz = new Spieler();
		return instanz;
	}

	@Override
	public void zeichnen(Graphics2D g) {
		checkInput();
		/*
		 * int x = (int) (posImFeld.getX() * 32), y = (int) (posImFeld.getY() *
		 * 32); g.setStroke(new BasicStroke(1)); g.setColor(Color.PINK);
		 * g.drawRect(x, y, (int) 32, (int) 32);
		 */

		// if (ges.mag() < .5)
		g.drawImage(bildDrehen(grafik), (int) (posImFeld.getX() * 32), (int) (posImFeld.getY() * 32), (int) 32,
				(int) 32, null);
		/*
		 * else g.drawImage(bildDrehen(animation[index]), (int)
		 * (posImFeld.getX() * 32), (int) (posImFeld.getY() * 32), (int) 32,
		 * (int) 32, null);
		 */

		for (int i = 0; i < cooldown.length; i++) {
			if (cooldown[i] > 0)
				cooldown[i] = (short) (cooldown[i] - 1);
		}

		if (Spiel.gibInstanz().ticks % 5 == 0)
			index++;
	}

	private void checkInput() {
		for (int i = 0; i < 4; i++) {
			if (EingabeManager.istAktiv(i))
				bewegen(Richtung.values()[i]);
		}
		if (EingabeManager.istAktiv(4))
			schuss();
	}

	private void schuss() {
		
	}

	private Image bildDrehen(BufferedImage grafik) {
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(blick.winkel), grafik.getWidth() / 2, grafik.getHeight() / 2);

		AffineTransformOp operation = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);

//		return operation.filter(grafik, null);
		return grafik;
	}

	private void bewegen(Richtung r) {
		blick = r;
		if (bewegungMoeglich(r) && cooldown[Richtung.getIndex(r)] == 0) {
			EventManager.gibInstanz().eventUebergeben(new BewegungsEvent(posImFeld, posImFeld.add(r.vektor), r));
			pos = new Vektor(posImFeld.getX() * Spiel.feldLaenge, posImFeld.getY() * Spiel.feldLaenge);
			cooldown[Richtung.getIndex(r)] = delay;
		}
	}

	/**
	 * Diese Methode �berpr�ft, ob die Bewegung, die der Spieler machen mag,
	 * �berhaupt m�glich ist
	 * 
	 * @param r
	 * @return
	 */
	private boolean bewegungMoeglich(Richtung r) {
		Vektor v = posImFeld.add(r.vektor, Rechenmethode.kopieren);
		return v.getX() >= 0 && v.getY() >= 0 && v.getX() < Spiel.spalten && v.getY() < Spiel.zeilen
				&& !Spiel.gibInstanz().getLevel().getFeld(v).isSolide();
	}

}
