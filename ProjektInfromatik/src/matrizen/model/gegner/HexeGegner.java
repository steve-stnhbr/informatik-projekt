package matrizen.model.gegner;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import matrizen.core.DateiManager;
import matrizen.core.Richtung;
import matrizen.core.Utils;
import matrizen.core.Vektor;
import matrizen.core.DateiManager.Bild;
import matrizen.model.Levelelement;
import matrizen.model.Spiel;
import matrizen.model.elemente.Gegner;
import matrizen.model.elemente.Geschoss;
import matrizen.model.elemente.Geschoss.Typ;
import matrizen.model.elemente.Spieler;

public class HexeGegner extends Gegner {
	private final int bewegungDelay = DateiManager.werte.get("hexe_delay_bewegung"),
			angriffDelay = DateiManager.werte.get("hexe_delay_angriff"),
			spezialDelay = DateiManager.werte.get("hexe_delay_spezial"),
			maxLeben = DateiManager.werte.get("hexe_leben"), maxSpawn = DateiManager.werte.get("hexe_spawn_max"),
			minSpawn = DateiManager.werte.get("hexe_spawn_min"), schaden = DateiManager.werte.get("hexe_schaden"),
			weite = DateiManager.werte.get("hexe_weite");

	private final float partikelGeschw = DateiManager.werte.get("hexe_partikel_geschw") / 10;

	private boolean aktiv;
	private Vektor ziel;
	private Richtung blick;

	public HexeGegner(Vektor feldPos, boolean aktiv) {
		grafik = DateiManager.laden(Bild.figurHexe);
		pos = feldPos.mult(Spiel.feldLaenge);
		leben = maxLeben;
		ziel = pos;
		blick = Richtung.OBEN;
	}

	@Override
	public void zeichnen(Graphics2D g) {
		g.drawImage(bildDrehen(grafik), (int) pos.getX(), (int) pos.getY(), (int) Spiel.feldLaenge,
				(int) Spiel.feldLaenge, null);

		if (Spiel.gibInstanz().ticks % bewegungDelay == 0)
			bewegen();
		else if (Spiel.gibInstanz().ticks % angriffDelay == 0)
			angriff();
		else if (Spiel.gibInstanz().ticks % spezialDelay == 0)
			spezialAttacke();
	}

	private void spezialAttacke() {
		int r = Utils.random(minSpawn, maxSpawn);
		while (r > 0) {
			spawnFledermaus();
			r--;
		}
	}

	private void spawnFledermaus() {
		Vektor v = new Vektor(Utils.random(3) - 2, Utils.random(3) - 2);
		Spiel.gibInstanz().getLevel().hinzufuegen(new FledermausGegner(v.add(pos.kopieren().div(Spiel.feldLaenge))));
	}

	private void bewegen() {
		if (pos.equals(ziel) && Spieler.gibInstanz().getPos().dist(pos) > Spiel.feldLaenge) {
			Vektor z = Spieler.gibInstanz().getPos().kopieren().sub(pos);

			if (Math.abs(z.getX()) > Math.abs(z.getY()))
				ziel = new Vektor(Math.signum(z.getX()), 0).mult(Spiel.feldLaenge).add(pos);
			else
				ziel = new Vektor(0, Math.signum(z.getY())).mult(Spiel.feldLaenge).add(pos);

			blick = Richtung.getRichtung(pos, ziel);

			if (bewegungMoeglich(ziel))
				pos = ziel;
			else
				ziel = pos;
		}
	}

	private Image bildDrehen(BufferedImage b) {
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(blick.getWinkel()), 16, 16);

		AffineTransformOp operation = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return operation.filter(grafik, null);
	}

	public boolean isAktiv() {
		return aktiv;
	}

	@Override
	public void angriff() {
		Spiel.gibInstanz().getLevel().hinzufuegen(
				new Geschoss(Typ.kleinOrange, schaden, weite, pos, new Vektor(1, 0).mult(partikelGeschw), this));
	}

	@Override
	public void beimTod() {

	}

	private boolean bewegungMoeglich(Vektor v) {
		// return !Spiel.gibInstanz().getLevel().istGegner(v.div(32))
		// && !Spiel.gibInstanz().getLevel().getFeld(v.div(32)).isSolide();
		return !Spieler.gibInstanz().getZiel().equals(v);
	}

}
