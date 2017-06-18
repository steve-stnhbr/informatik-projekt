package matrizen.model.gegner;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import matrizen.core.DateiManager;
import matrizen.core.DateiManager.Bild;
import matrizen.core.Richtung;
import matrizen.core.Utils;
import matrizen.core.Vektor;
import matrizen.model.Spiel;
import matrizen.model.elemente.Gegner;
import matrizen.model.elemente.Spieler;

public class FledermausGegner extends Gegner {
	private final int bewegungDelay = DateiManager.werte.get("fledermaus_bewegung_delay"),
			schaden = DateiManager.werte.get("fledermaus_schaden"),
			bewegungGeschw = DateiManager.werte.get("fledermaus_bewegung_geschw"),
			lebenMax = DateiManager.werte.get("fledermaus_lebenszeit_max"),
			lebenMin = DateiManager.werte.get("fledermaus_lebenszeit_min"),
			maxLeben = DateiManager.werte.get("fledermaus_leben"),
			angriffDelay = DateiManager.werte.get("fledermaus_angriff_delay");

	private int lebenszeit, bewegung;
	private boolean inaktiv;
	private Vektor ziel;
	private Richtung blick;

	public FledermausGegner(Vektor feldPos) {
		grafik = DateiManager.laden(Bild.figurFledermaus);
		pos = feldPos.mult(Spiel.feldLaenge);
		lebenszeit = Utils.random(lebenMin, lebenMax);
		leben = maxLeben;
		blick = Richtung.OBEN;
		ziel = pos;
	}

	@Override
	public void zeichnen(Graphics2D g) {
		super.aktualisieren();

		if (inaktiv)
			g.setComposite(makeComposite(.75f));
		g.drawImage(bildDrehen(grafik), (int) pos.getX(), (int) pos.getY(), (int) Spiel.feldLaenge,
				(int) Spiel.feldLaenge, null);
		g.setComposite(makeComposite(1f));

		if (Spiel.gibInstanz().ticks % angriffDelay == 0)
			angriff();
		else if (Spiel.gibInstanz().ticks % bewegungDelay == 0)
			bewegen();

		if (pos.dist(ziel) < ges.mag()) {
			ges = Vektor.nullVektor;
			pos = ziel;
		}

		if (lebenszeit == 0)
			inaktiv = true;

		lebenszeit--;
	}

	private AlphaComposite makeComposite(float alpha) {
		int type = AlphaComposite.SRC_OVER;
		return (AlphaComposite.getInstance(type, alpha));
	}

	private void bewegen() {
		System.out.println(ziel + ":" + pos + ":" + inaktiv + ":" + Spieler.gibInstanz().getPos().dist(pos));
		if (!inaktiv) {
			if (pos.equals(ziel) && Spieler.gibInstanz().getPos().dist(pos) > Spiel.feldLaenge) {
				Vektor v = Spieler.gibInstanz().getPos().kopieren().sub(pos), z = Vektor.nullVektor;

				if (Math.abs(v.getX()) > Math.abs(v.getY()))
					z = new Vektor(Math.signum(v.getX()), 0).mult(Spiel.feldLaenge).add(pos);
				else
					z = new Vektor(0, Math.signum(v.getY())).mult(Spiel.feldLaenge).add(pos);

				blick = Richtung.getRichtung(pos, ziel);

				if (bewegungMoeglich(z))
					ziel = z;

			}
		} else {
			if (bewegung == 0) {
				Vektor v = pos.kopieren().div(Spiel.feldLaenge);
				int abstandY = 0, abstandX = 0;

				if (v.getX() == Spiel.spalten / 2 && v.getY() == Spiel.zeilen / 2) {
					bewegung = Utils.random(-2, 2, 0);
					return;
				}

				abstandX = (int) (v.getX() > Spiel.spalten / 2 ? Spiel.spalten / 2 - v.getX()
						: v.getX() - Spiel.spalten / 2);
				abstandY = (int) (v.getY() > Spiel.zeilen / 2 ? Spiel.zeilen / 2 - v.getY()
						: v.getY() - Spiel.zeilen / 2);

				if (abstandX > abstandY)
					bewegung = (int) (2 * Math.signum(v.getX() - Spiel.spalten));
				else if (abstandY > abstandX)
					bewegung = (int) (Math.signum(v.getY() - Spiel.spalten));
				else
					bewegung = Utils.random(-2, 2, 0);
			} else {
				if (pos.equals(ziel)) {
					Vektor z = Vektor.nullVektor;

					if (bewegung == -2)
						z = pos.add(new Vektor(1, 0).mult(Spiel.feldLaenge));
					else if (bewegung == -1)
						z = pos.add(new Vektor(0, 1).mult(Spiel.feldLaenge));
					else if (bewegung == 1)
						z = pos.add(new Vektor(0, -1).mult(Spiel.feldLaenge));
					else if (bewegung == 2)
						z = pos.add(new Vektor(-1, 0).mult(Spiel.feldLaenge));

					blick = Richtung.getRichtung(pos, ziel);

					if (bewegungMoeglich(z))
						ziel = z;
				}
			}
		}

		pos = ziel;
	}

	private Image bildDrehen(BufferedImage b) {
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(blick.getWinkel()), 16, 16);

		AffineTransformOp operation = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return operation.filter(grafik, null);
	}

	public boolean angriff() {
		if (Spieler.gibInstanz().getPos().dist(pos) > Spiel.feldLaenge || inaktiv)
			return false;
		Spieler.gibInstanz().schaden(schaden);
		// Spiel.gibInstanz().getLevel().entfernen(this);
		inaktiv = true;
		return true;
	}

	@Override
	public void beimTod() {

	}

	private boolean bewegungMoeglich(Vektor v) {
		// return !Spiel.gibInstanz().getLevel().istGegner(v.div(32))
		// && !Spiel.gibInstanz().getLevel().getFeld(v.div(32)).isSolide();
		return !Spieler.gibInstanz().getZiel().equals(v);
	}

	public Vektor getZiel() {
		return ziel;
	}

}
