package matrizen.model.gegner;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import matrizen.core.DateiManager;
import matrizen.core.DateiManager.Bild;
import matrizen.core.Richtung;
import matrizen.core.Vektor;
import matrizen.model.Spiel;
import matrizen.model.elemente.Gegner;
import matrizen.model.elemente.Spieler;

public class ZombieGegner extends Gegner {
	private final int schaden = DateiManager.werte.get("zombie_schaden"),
			bewegungDelay = DateiManager.werte.get("zombie_bewegung_delay"),
			bewegungGeschw = DateiManager.werte.get("zombie_bewegung_geschw"),
			angriffDelay = DateiManager.werte.get("zombie_delay_angriff"),
			maxLeben = DateiManager.werte.get("zombie_leben");

	private Richtung blick;
	private Vektor ziel;

	public ZombieGegner(Vektor feldPos) {
		pos = feldPos.mult(32);
		grafik = DateiManager.laden(Bild.figurZombie);
		blick = Richtung.OBEN;
		ziel = pos;
		leben = maxLeben;
	}

	@Override
	public boolean angriff() {
		if (Spiel.gibInstanz().getLevel().istGegner(blick.getVektor().kopieren().add(pos), this)) {
			Spieler.gibInstanz().schaden(schaden);
			return true;
		}

		return false;
	}

	@Override
	public Vektor getZiel() {
		return ziel;
	}

	@Override
	public void beimTod() {

	}

	@Override
	public void zeichnen(Graphics2D g) {
		super.aktualisieren();

		g.drawImage(bildDrehen(grafik), (int) pos.getX(), (int) pos.getY(), Spiel.feldLaenge, Spiel.feldLaenge, null);

		if (Spiel.gibInstanz().ticks % bewegungDelay == 0)
			bewegen();
		else if (Spiel.gibInstanz().ticks % angriffDelay == 0)
			angriff();

		if (pos.dist(ziel) < ges.mag()) {
			ges = Vektor.nullVektor;
			pos = ziel;
		}
	}

	private void bewegen() {
		if (pos.equals(ziel) && Spieler.gibInstanz().getPos().dist(pos) > Spiel.feldLaenge) {
			Vektor v = Spieler.gibInstanz().getPos().kopieren().sub(pos), z = Vektor.nullVektor;

			if (Math.abs(v.getX()) > Math.abs(v.getY()))
				z = new Vektor(Math.signum(v.getX()), 0).mult(Spiel.feldLaenge).add(pos);
			else
				z = new Vektor(0, Math.signum(v.getY())).mult(Spiel.feldLaenge).add(pos);

			blick = Richtung.getRichtung(pos, z);

			if (bewegungMoeglich(z))
				ziel = z;
			if (!pos.equals(ziel))
				bes = ziel.kopieren().sub(pos).normalize().mult(bewegungGeschw / 10);
		} else if (pos.equals(ziel) && Spieler.gibInstanz().getPos().dist(pos) <= Spiel.feldLaenge) {
			blick = Richtung.getRichtung(pos, Spieler.gibInstanz().getPos());
		}
	}

	private boolean bewegungMoeglich(Vektor v) {
		// return !Spiel.gibInstanz().getLevel().istGegner(v.div(32))
		// && !Spiel.gibInstanz().getLevel().getFeld(v.div(32)).isSolide();
		return !Spieler.gibInstanz().getZiel().equals(v);
	}

	private Image bildDrehen(BufferedImage b) {
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(blick.getWinkel()), 16, 16);

		AffineTransformOp operation = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return operation.filter(grafik, null);
	}

}
