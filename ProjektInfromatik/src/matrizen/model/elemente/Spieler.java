package matrizen.model.elemente;

import static matrizen.core.DateiManager.werte;
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
import matrizen.core.DateiManager.Bild;
import matrizen.core.EingabeManager;
import matrizen.core.Richtung;
import matrizen.core.Vektor;
import matrizen.core.event.BewegungsEvent;
import matrizen.core.event.EventManager;
import matrizen.model.Spiel;
import matrizen.model.Zauberstab;
import matrizen.model.elemente.Item.Typ;
import matrizen.model.zauberstaebe.BlitzZauberstab;
import matrizen.model.zauberstaebe.EinfachZauberstab;
import matrizen.model.zauberstaebe.MehrfachZauberstab;
import matrizen.model.zauberstaebe.VerfolgungsZauberstab;

/**
 * Diese Klasse repräsentiert den Spieler
 */
public class Spieler extends Figur {
	private static Spieler instanz;

	public final int maxLeben = werte.get("spieler_leben"), angriff = werte.get("spieler_delay_angriff"),
			zielMuenzen = DateiManager.werte.get("spiel_ziel_muenzen");

	private Vektor ziel;
	private int index, xFeld, yFeld;
	private short delay = (short) werte.get("spieler_delay_bewegung");
	private short[] cooldown;
	private Richtung blick = Richtung.OBEN;
	private List<Item> inventar;
	private Zauberstab stab, stabDavor;
	private float magDavor;
	private List<Zauberstab> staebe;
	private Vektor[] farbPositionen = { new Vektor(29, 8), new Vektor(29, 9), new Vektor(28, 7), new Vektor(28, 8),
			new Vektor(28, 9), new Vektor(28, 10), new Vektor(27, 7), new Vektor(27, 8), new Vektor(27, 9),
			new Vektor(27, 10), new Vektor(9, 8), new Vektor(10, 8), new Vektor(11, 8), new Vektor(12, 8),
			new Vektor(13, 8), new Vektor(18, 8), new Vektor(15, 8), new Vektor(16, 8), new Vektor(17, 8),
			new Vektor(17, 9), new Vektor(17, 10), new Vektor(18, 10), new Vektor(19, 10), new Vektor(19, 11),
			new Vektor(20, 11), new Vektor(20, 12), new Vektor(20, 13), new Vektor(20, 14), new Vektor(20, 15),
			new Vektor(20, 16), new Vektor(20, 17), new Vektor(20, 18), new Vektor(19, 18), new Vektor(19, 19),
			new Vektor(18, 19), new Vektor(17, 19), new Vektor(17, 20), new Vektor(17, 21), new Vektor(16, 21),
			new Vektor(15, 21), new Vektor(14, 21), new Vektor(13, 21), new Vektor(12, 21), new Vektor(11, 21),
			new Vektor(10, 21), new Vektor(9, 21), new Vektor(9, 20), new Vektor(9, 19), new Vektor(8, 19),
			new Vektor(8, 18), new Vektor(7, 18), new Vektor(6, 18), new Vektor(6, 17), new Vektor(5, 16),
			new Vektor(6, 15), new Vektor(6, 14), new Vektor(6, 12), new Vektor(6, 11), new Vektor(7, 11),
			new Vektor(8, 11), new Vektor(8, 10), new Vektor(9, 10), new Vektor(9, 9), new Vektor(10, 9),
			new Vektor(11, 9), new Vektor(12, 9), new Vektor(13, 9), new Vektor(14, 9), new Vektor(15, 9),
			new Vektor(16, 9), new Vektor(16, 10), new Vektor(16, 11), new Vektor(17, 11), new Vektor(18, 11),
			new Vektor(18, 12), new Vektor(19, 12), new Vektor(19, 13), new Vektor(19, 14), new Vektor(19, 15),
			new Vektor(19, 16), new Vektor(19, 17), new Vektor(18, 17), new Vektor(18, 18), new Vektor(17, 18),
			new Vektor(16, 18), new Vektor(16, 19), new Vektor(16, 20), new Vektor(15, 20), new Vektor(14, 20),
			new Vektor(13, 20), new Vektor(12, 20), new Vektor(11, 20), new Vektor(10, 20), new Vektor(10, 19),
			new Vektor(10, 18), new Vektor(9, 18), new Vektor(9, 17), new Vektor(8, 17), new Vektor(7, 17),
			new Vektor(7, 16), new Vektor(7, 15), new Vektor(7, 14), new Vektor(7, 13), new Vektor(7, 12),
			new Vektor(8, 12), new Vektor(9, 12), new Vektor(9, 11), new Vektor(10, 11), new Vektor(10, 10) };

	private Spieler() {
		cooldown = new short[6];
		grafik = DateiManager.laden(DateiManager.Bild.figurSpieler);
		pos = new Vektor((float) Math.floor(Spiel.spalten / 2) * Spiel.feldLaenge,
				(float) Math.floor(Spiel.zeilen / 2) * Spiel.feldLaenge);
		ziel = pos;
		inventar = new ArrayList<Item>();
		animation = new BufferedImage[] { DateiManager.laden(Bild.figurSpielerAnim0),
				DateiManager.laden(Bild.figurSpielerAnim1) };
		staebe = new ArrayList<>();
		staebe.add(EinfachZauberstab.gibInstanz());
		stab = staebe.get(0);
		leben = maxLeben;
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

		if (leben > maxLeben)
			leben = maxLeben;

		if (leben <= 0)
			Spiel.gibInstanz().beenden();

		if (!stab.equals(stabDavor))
			zauberstabZeichnen();

		if (ges.mag() == 0 && magDavor == 0)
			g.drawImage(bildDrehen(grafik), (int) pos.getX(), (int) pos.getY(), (int) Spiel.feldLaenge,
					(int) Spiel.feldLaenge, null);
		else
			g.drawImage(bildDrehen(animation[index]), (int) pos.getX(), (int) pos.getY(), (int) Spiel.feldLaenge,
					(int) Spiel.feldLaenge, null);

		stab.aktualisieren();

		for (int i = 0; i < cooldown.length; i++) {
			if (cooldown[i] > 0)
				cooldown[i] = (short) (cooldown[i] - 1);
		}

		if (Spiel.gibInstanz().ticks % 5 == 0) {
			index++;
			if (index == animation.length)
				index = 0;
		}

		if (pos.dist(ziel) < .5) {
			ges.mult(0);
			pos = ziel;
		}

		if (gibAnzahlMuenzen() == zielMuenzen)
			Spiel.gibInstanz().geschafft();

		if ((ges.getX() != 0 && ges.getY() != 0)) {
			logger.log(Level.WARNING, "Bewegung korrigiert");
			logger.log(Level.WARNING, "Ziel davor: " + ziel.toString());
			logger.log(Level.WARNING, "Position davor: " + pos.toString());

			/*
			 * ges.setY(0);
			 * 
			 * if(((int)pos.getX()) % Spiel.feldLaenge != 0) pos.setX(((int)
			 * pos.getX() / Spiel.feldLaenge) * Spiel.feldLaenge);
			 * if(((int)pos.getY()) % Spiel.feldLaenge != 0) pos.setY(((int)
			 * pos.getY() / Spiel.feldLaenge) * Spiel.feldLaenge);
			 * 
			 * if(pos.getY() < 0) pos.setY(0); if(pos.getX() < 0) pos.setX(0);
			 * 
			 * if(ziel.getX() < pos.getX()) ziel.setX((((int)pos.getX() /
			 * Spiel.feldLaenge) + 1) * Spiel.feldLaenge); else if(ziel.getX() >
			 * pos.getX()) ziel.setX((((int)pos.getX() / Spiel.feldLaenge) - 1)
			 * * Spiel.feldLaenge);
			 * 
			 * if(ziel.getY() < pos.getY()) ziel.setY((((int)pos.getY() /
			 * Spiel.feldLaenge) + 1) * Spiel.feldLaenge); if(ziel.getY() >
			 * pos.getY()) ziel.setY((((int)pos.getY() / Spiel.feldLaenge) - 1)
			 * * Spiel.feldLaenge);
			 */

			logger.log(Level.WARNING, "Ziel danach: " + ziel.toString());
			logger.log(Level.WARNING, "Position danach: " + pos.toString());
		}

		magDavor = ges.mag();
		stabDavor = stab;
	}

	private void zauberstabZeichnen() {
		for (Vektor v : farbPositionen) {
			grafik.setRGB((int) v.getX(), (int) v.getY(), stab.getFarbe().getRGB());
			for (BufferedImage bImg : animation)
				bImg.setRGB((int) v.getX(), (int) v.getY(), stab.getFarbe().getRGB());
		}

	}

	public static void reset() {
		instanz = new Spieler();
	}

	private void checkInput() {
		for (int i = 0; i < 4; i++)
			if (EingabeManager.istAktiv(i))
				bewegen(Richtung.values()[i]);

		if (EingabeManager.istAktiv(4))
			schuss();
		if (EingabeManager.istAktiv(5))
			waffeWechseln();
	}

	private void waffeWechseln() {
		if (cooldown[5] == 0) {
			if (staebe.indexOf(stab) + 1 >= staebe.size())
				stab = staebe.get(0);
			else
				stab = staebe.get(staebe.indexOf(stab) + 1);

			cooldown[5] = 5;
		}
	}

	private void schuss() {
		if (Spiel.gibInstanz().tutorial) {
			if (!Spiel.gibInstanz().tutorials[1])
				Spiel.gibInstanz().tutorialTick = (int) Spiel.gibInstanz().ticks;
			Spiel.gibInstanz().tutorials[1] = true;
		}

		if (cooldown[4] == 0) {
			stab.schuss();
			cooldown[4] = (short) stab.getDelay();
		}
	}

	private Image bildDrehen(BufferedImage grafik) {
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(blick.getWinkel()), grafik.getWidth() / 2, grafik.getHeight() / 2);

		AffineTransformOp operation = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
		if (ges.kopieren().normalize().equals(blick.getVektor().normalize()) || ges.equals(Vektor.nullVektor))
			return operation.filter(grafik, null);
		else
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
			ziel = new Vektor(xFeld, yFeld).mult(Spiel.feldLaenge);
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
			return v.getX() >= 0 && v.getY() >= 0 && v.getX() < Spiel.feldLaenge * Spiel.spalten
					&& v.getY() < Spiel.feldLaenge * Spiel.zeilen
					&& !Spiel.gibInstanz().getLevel().getFeld(v).isSolide()
					&& !Spiel.gibInstanz().getLevel().istGegnerSpieler(v, this);
		} catch (Exception e) {
		}
		return false;
	}

	public void aufsammeln(Item i) {
		switch (i.getTyp()) {
		case stabBlitz:
			staebe.add(BlitzZauberstab.gibInstanz());
			stab = staebe.get(staebe.indexOf(BlitzZauberstab.gibInstanz()));
			break;
		case stabDreifach:
			staebe.add(MehrfachZauberstab.gibInstanz());
			stab = staebe.get(staebe.indexOf(MehrfachZauberstab.gibInstanz()));
			break;
		case stabVerfolgung:
			staebe.add(VerfolgungsZauberstab.gibInstanz());
			stab = staebe.get(staebe.indexOf(VerfolgungsZauberstab.gibInstanz()));
			break;
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

	public Richtung getBlick() {
		return blick;
	}

	public void setBlick(Richtung blick) {
		this.blick = blick;
	}

	public Vektor getZiel() {
		return ziel;
	}

	public void setZiel(Vektor ziel) {
		this.ziel = ziel;
	}

	public List<Item> getInventar() {
		return inventar;
	}

	public void setInventar(List<Item> inventar) {
		this.inventar = inventar;
	}

	public Zauberstab getStab() {
		return stab;
	}

	public void setStab(Zauberstab stab) {
		this.stab = stab;
	}

	public int gibAnzahlMuenzen() {
		int m = 0;

		for (Item l : inventar)
			if (l.getTyp() == Typ.muenze)
				m++;
		return m;
	}

	public boolean hatSchluessel() {
		for (Item i : inventar)
			if (i.getTyp() == Typ.schluessel)
				return true;

		return false;
	}

	public void schluesselEntfernen() {
		for (int i = 0; i < inventar.size(); i++)
			if (inventar.get(i).getTyp() == Typ.schluessel)
				inventar.remove(i);
	}

}