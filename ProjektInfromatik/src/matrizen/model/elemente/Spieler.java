package matrizen.model.elemente;

import static matrizen.view.SpielFenster.logger;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import matrizen.core.DateiManager;
import matrizen.core.EingabeManager;
import matrizen.core.Richtung;
import matrizen.core.Vektor;
import matrizen.core.event.BewegungsEvent;
import matrizen.core.event.EventManager;
import matrizen.model.Spiel;
import matrizen.model.elemente.Geschoss.Typ;
import matrizen.view.SpielFenster;

/**
 * Diese Klasse repräsentiert den Spieler
 */
public class Spieler extends Figur {
	private static Spieler instanz;

	private Vektor ziel;
	private int index, xFeld, yFeld;
	short delay = 7, delaySchuss = 20;
	private short[] cooldown;
	private Richtung blick = Richtung.OBEN;
	private List<Item> inventar;

	private Spieler() {
		cooldown = new short[5];
		grafik = DateiManager.laden(DateiManager.Bild.figurSpieler);
		pos = new Vektor((float) Math.floor(Spiel.spalten / 2) * 32, (float) Math.floor(Spiel.zeilen / 2) * 32);
		ziel = pos;
		inventar = new ArrayList<Item>();
		animation = new BufferedImage[] { DateiManager.laden(DateiManager.Bild.figurSpielerAnim0) };
	}

	public static Spieler gibInstanz() {
		if (instanz == null)
			instanz = new Spieler();
		return instanz;
	}

	@Override
	public void zeichnen(Graphics2D g) {
		super.aktualisieren();
		checkInput();

		if (ges.mag() == 0)
			g.drawImage(grafik, (int) pos.getX(), (int) pos.getY(), (int) 32, (int) 32, null);
		else
			g.drawImage(animation[index], (int) pos.getX(), (int) pos.getY(), 32, 32, null);

		for (int i = 0; i < cooldown.length; i++) {
			if (cooldown[i] > 0)
				cooldown[i] = (short) (cooldown[i] - 1);
		}

		if (Spiel.gibInstanz().ticks % 20 == 0) {
			index++;
			if (index == animation.length)
				index = 0;
		}

		if (pos.dist(ziel) < .5) {
			ges.mult(0);
			pos = ziel;
		}

		if ((ges.getX() != 0 && ges.getY() != 0)) {
			logger.log(Level.WARNING, "Bewegung korrigiert");
			logger.log(Level.WARNING, "Ziel davor: " + ziel.toString());
			logger.log(Level.WARNING, "Position davor: " + pos.toString());

			/*
			 * ges.setY(0);
			 * 
			 * if(((int)pos.getX()) % 32 != 0) pos.setX(((int) pos.getX() / 32)
			 * * 32); if(((int)pos.getY()) % 32 != 0) pos.setY(((int) pos.getY()
			 * / 32) * 32);
			 * 
			 * if(pos.getY() < 0) pos.setY(0); if(pos.getX() < 0) pos.setX(0);
			 * 
			 * if(ziel.getX() < pos.getX()) ziel.setX((((int)pos.getX() / 32) +
			 * 1) * 32); else if(ziel.getX() > pos.getX())
			 * ziel.setX((((int)pos.getX() / 32) - 1) * 32);
			 * 
			 * if(ziel.getY() < pos.getY()) ziel.setY((((int)pos.getY() / 32) +
			 * 1) * 32); if(ziel.getY() > pos.getY())
			 * ziel.setY((((int)pos.getY() / 32) - 1) * 32);
			 */

			logger.log(Level.WARNING, "Ziel danach: " + ziel.toString());
			logger.log(Level.WARNING, "Position danach: " + pos.toString());
		}
	}

	private void checkInput() {
		for (int i = 0; i < 4; i++)
			if (EingabeManager.istAktiv(i))
				bewegen(Richtung.values()[i]);

		if (EingabeManager.istAktiv(4))
			schuss();
	}

	private void schuss() {
		if (cooldown[4] == 0) {
			// zauberstab.schuss();
			Spiel.gibInstanz().getLevel().hinzufuegen(new Geschoss(Typ.stern, 10, this.pos,
					new Vektor(blick.getVektor().getX(), blick.getVektor().getY()).mult(5), true));
			cooldown[4] = delaySchuss;
		}
	}

	private Image bildDrehen(BufferedImage grafik) {
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(blick.getWinkel()), grafik.getWidth() / 2, grafik.getHeight() / 2);

		AffineTransformOp operation = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);

		// return operation.filter(grafik, null);
		return grafik;
	}

	private void bewegen(Richtung r) {
		blick = r;
		if (bewegungMoeglich(r) && cooldown[Richtung.getIndex(r)] == 0 && !(ges.getX() != 0 && ges.getY() != 0)
				&& ges.mag() < 1) {
			EventManager.gibInstanz().eventUebergeben(new BewegungsEvent(new Vektor(xFeld, yFeld),
					new Vektor(xFeld, yFeld).add(r.getVektor()).kopieren(), r));
			xFeld += r.getVektor().getX();
			yFeld += r.getVektor().getY();
			ziel = new Vektor(xFeld, yFeld).mult(32f);
			bes = ziel.kopieren().sub(pos).mult(.125f);

			for (int i = 0; i < 4; i++) {
				cooldown[i] = delay;
			}
		}
	}

	/**
	 * Diese Methode überprüft, ob die Bewegung, die der Spieler machen mag,
	 * überhaupt möglich ist
	 */
	private boolean bewegungMoeglich(Richtung r) {
		Vektor v = new Vektor(xFeld + r.getVektor().getX(), yFeld + r.getVektor().getY());

		try {
			return v.getX() >= 0 && v.getY() >= 0 && v.getX() < SpielFenster.hoehe / 32
					&& v.getY() < SpielFenster.breite / 32 && !Spiel.gibInstanz().getLevel().getFeld(v).isSolide()
					&& !Spiel.gibInstanz().getLevel().istGegner(v);
		} catch (Exception e) {
		}
		return false;
	}

	public void aufsammeln(Item i) {
		switch (i.getTyp()) {
		case herz:
			leben += 20;
		default:
			inventar.add(i);
		}
	}

	@Override
	public void beimTod() {
		Spiel.gibInstanz().beenden();
	}

	public int getxFeld() {
		return xFeld;
	}

	public void setxFeld(int xFeld) {
		this.xFeld = xFeld;
	}

	public int getyFeld() {
		return yFeld;
	}

	public void setyFeld(int yFeld) {
		this.yFeld = yFeld;
	}

}
