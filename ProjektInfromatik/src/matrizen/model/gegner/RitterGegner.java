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
			delayBewegung = werte.get("ritter_delay_bewegung"), geschw = werte.get("ritter_bewegung_geschw");
	private int drehung;
	private Vektor ziel;
	private BufferedImage drehBild;
	private Richtung blick;

	public RitterGegner(Vektor feldPos) {
		pos = feldPos.mult(32);
		grafik = DateiManager.laden(Bild.figurRitterDrehung);
		animation = new BufferedImage[] { DateiManager.laden(Bild.figurRitterAnim0),
				DateiManager.laden(Bild.figurRitterAnim1) };
		drehBild = DateiManager.laden(Bild.figurRitterDrehung);
		ziel = pos;
		blick = Richtung.OBEN;
	}

	@Override
	public void angriff() {
		if (Spieler.gibInstanz().getPos().dist(pos) <= 32) {
			drehung = 360;
			Spieler.gibInstanz().schaden(schaden);
		}
	}

	@Override
	public void beimTod() {

	}

	@Override
	public void zeichnen(Graphics2D g) {
		super.aktualisieren();
		if (drehung > 0) 
			g.drawImage(bildDrehen(drehBild, Math.toRadians(drehung)), (int) pos.getX(), (int) pos.getY(), 32, 32,
					null);
		else
			g.drawImage(bildDrehen(grafik, Math.toRadians(blick.getWinkel())), (int) pos.getX(), (int) pos.getY(), 32,
					32, null);

		if (Spiel.gibInstanz().ticks % delayBewegung == 0 && pos.equals(ziel))
			bewegen();
		if (Spiel.gibInstanz().ticks % delayAngriff == 0)
			angriff();

		if (ziel.equals(pos))
			bes = ziel.kopieren().sub(pos);

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
		if (pos.equals(ziel) && Spieler.gibInstanz().getPos().dist(pos) > 32 && drehung == 0) {
			Vektor z = Spieler.gibInstanz().getPos().kopieren().sub(pos);

			if (Math.abs(z.getX()) > Math.abs(z.getY()))
				ziel = new Vektor(Math.signum(z.getX()), 0).mult(32).add(pos);
			else
				ziel = new Vektor(0, Math.signum(z.getY())).mult(32).add(pos);

			blick = Richtung.getRichtung(pos, ziel);

			if (bewegungMoeglich(ziel))
				pos = ziel;
			else
				ziel = pos;
		}
	}

	private boolean bewegungMoeglich(Vektor v) {
		// return !Spiel.gibInstanz().getLevel().istGegner(v.div(32))
		// && !Spiel.gibInstanz().getLevel().getFeld(v.div(32)).isSolide();
		return !Spieler.gibInstanz().getZiel().equals(v);
	}

}
