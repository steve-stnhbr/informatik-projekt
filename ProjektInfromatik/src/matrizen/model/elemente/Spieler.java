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
 * Diese Klasse repräsentiert den Spieler
 */
public class Spieler extends Figur {
	private static Spieler instanz;

	private Vektor ziel;
	private int index, xFeld, yFeld;
	short delay = 7;
	private short[] cooldown;
	private Richtung blick = Richtung.OBEN;

	private Spieler() {
		cooldown = new short[5];
		grafik = DateiManager.laden(DateiManager.Bild.elementSpieler);
		ziel = pos;
	}

	public static Spieler gibInstanz() {
		if (instanz == null)
			instanz = new Spieler();
		return instanz;
	}

	@Override
	public void zeichnen(Graphics2D g) {
		super.zeichnen(g);
		checkInput();

		g.drawImage(grafik, (int) pos.getX(), (int) pos.getY(), (int) 32, (int) 32, null);

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

		// return operation.filter(grafik, null);
		return grafik;
	}

	private void bewegen(Richtung r) {
		blick = r;
		if (bewegungMoeglich(r) && cooldown[Richtung.getIndex(r)] == 0) {
			EventManager.gibInstanz().eventUebergeben(
					new BewegungsEvent(new Vektor(xFeld, yFeld), new Vektor(xFeld, yFeld).add(r.vektor).kopieren(), r));
			xFeld += r.vektor.getX();
			yFeld += r.vektor.getY();
			ziel = new Vektor(xFeld, yFeld).mult(32f);
			pos = ziel;
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
		Vektor v = new Vektor(xFeld + r.vektor.getX(), yFeld + r.vektor.getY());
		return v.getX() >= 0 && v.getY() >= 0 && v.getX() < Spiel.spalten && v.getY() < Spiel.zeilen
				&& !Spiel.gibInstanz().getLevel().getFeld(v).isSolide();
	}

}
