package matrizen.model.gegner;

import static matrizen.core.DateiManager.werte;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import matrizen.core.DateiManager;
import matrizen.core.Richtung;
import matrizen.core.DateiManager.Bild;
import matrizen.core.Vektor;
import matrizen.model.Spiel;
import matrizen.model.elemente.Gegner;
import matrizen.model.elemente.Spieler;

public class RitterGegner extends Gegner {
	public final int maxLeben = werte.get("ritter_leben"), delayAngriff = werte.get("ritter_delay_angriff"),
			drehGeschw = werte.get("ritter_drehung_geschw"), schaden = werte.get("ritter_schaden"),
			delayBewegung = werte.get("ritter_delay_bewegung"), bewegungGeschw = werte.get("ritter_bewegung_geschw");
	private int drehung;
	private Vektor ziel;
	private BufferedImage drehBild;
	private Richtung blick;

	public RitterGegner(Vektor feldPos) {
		pos = feldPos.mult(Spiel.feldLaenge);
		grafik = DateiManager.laden(Bild.figurRitterDrehung);
		animation = new BufferedImage[] { DateiManager.laden(Bild.figurRitterAnim0),
				DateiManager.laden(Bild.figurRitterAnim1) };
		drehBild = DateiManager.laden(Bild.figurRitterDrehung);
		ziel = pos;
		blick = Richtung.OBEN;
		leben = maxLeben;
	}

	@Override
	public boolean angriff() {
		if (Spieler.gibInstanz().getPos().dist(pos) <= Spiel.feldLaenge) {
			drehung = 360;
			Spieler.gibInstanz().schaden(schaden);
			return true;
		}

		return false;
	}

	@Override
	public void beimTod() {

	}

	@Override
	public void zeichnen(Graphics2D g) {
		super.aktualisieren();
		System.out.println(pos);
		if (drehung >= 0)
			g.drawImage(bildDrehen(drehBild, Math.toRadians(drehung)), (int) pos.getX(), (int) pos.getY(),
					(int) Spiel.feldLaenge, (int) Spiel.feldLaenge, null);
		else
			g.drawImage(bildDrehen(grafik, Math.toRadians(blick.getWinkel())), (int) pos.getX(), (int) pos.getY(),
					(int) Spiel.feldLaenge, (int) Spiel.feldLaenge, null);

		if (Spiel.gibInstanz().ticks % delayAngriff == 0 && !angriff())
			;
		else if (Spiel.gibInstanz().ticks % delayBewegung == 0 && pos.equals(ziel))
			bewegen();

		if (ziel.dist(pos) < ges.mag()) {
			ges = Vektor.nullVektor;
			pos = ziel;
		}

		if (drehung > 0)
			drehung -= drehGeschw;
		if (drehung < 0)
			drehung = 0;
	}

	private Image bildDrehen(BufferedImage b, double d) {
		AffineTransform transform = new AffineTransform();
		transform.rotate(d, 16, 16);

		AffineTransformOp operation = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return operation.filter(grafik, null);
	}

	private void bewegen() {
		if (pos.equals(ziel) && Spieler.gibInstanz().getPos().dist(pos) > Spiel.feldLaenge && drehung == 0) {
			Vektor v = Spieler.gibInstanz().getPos().kopieren().sub(pos), z = Vektor.nullVektor;

			if (Math.abs(v.getX()) > Math.abs(v.getY()))
				z = new Vektor(Math.signum(v.getX()), 0).mult(Spiel.feldLaenge).add(pos);
			else
				z = new Vektor(0, Math.signum(v.getY())).mult(Spiel.feldLaenge).add(pos);

			blick = Richtung.getRichtung(pos, ziel);

			if (bewegungMoeglich(z))
				ziel = z;
			
			if (!ziel.equals(pos))
				ges = ziel.kopieren().sub(pos).normalize().mult(bewegungGeschw / 10);
		}
	}

	private boolean bewegungMoeglich(Vektor v) {
		// return
		// !Spiel.gibInstanz().getLevel().istGegner(v.div(Spiel.feldLaenge))
		// &&
		// !Spiel.gibInstanz().getLevel().getFeld(v.div(Spiel.feldLaenge)).isSolide();
		return !Spieler.gibInstanz().getZiel().equals(v) && !Spiel.gibInstanz().getLevel().istGegner(v, this);
	}
	
	public Vektor getZiel() {
		return ziel;
	}

}
