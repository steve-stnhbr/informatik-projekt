package matrizen.model.gegner;

import static matrizen.core.DateiManager.werte;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import matrizen.core.DateiManager;
import matrizen.core.Richtung;
import matrizen.core.Utils;
import matrizen.core.DateiManager.Bild;
import matrizen.core.Vektor;
import matrizen.model.Level;
import matrizen.model.Spiel;
import matrizen.model.elemente.Gegner;
import matrizen.model.elemente.Item;
import matrizen.model.elemente.Spieler;

public class RitterGegner extends Gegner {
	public final int maxLeben = werte.get("ritter_leben"), delayAngriff = werte.get("ritter_delay_angriff"),
			drehGeschw = werte.get("ritter_drehung_geschw"), schaden = werte.get("ritter_schaden"),
			delayBewegung = werte.get("ritter_delay_bewegung"), bewegungGeschw = werte.get("ritter_bewegung_geschw"),
			dropMuenze = werte.get("ritter_drop_muenze"), dropHerz = werte.get("ritter_drop_herz");

	private int drehung;
	private Vektor ziel;
	private BufferedImage drehBild;
	private Richtung blick;

	public RitterGegner(Vektor feldPos) {
		pos = feldPos.mult(Spiel.feldLaenge);
		animation = new BufferedImage[] { DateiManager.laden(Bild.figurRitterAnim0),
				DateiManager.laden(Bild.figurRitterAnim1) };
		drehBild = DateiManager.laden(Bild.figurRitterDrehung);
		grafik = DateiManager.laden(Bild.figurRitterDrehung);
		ziel = pos;
		blick = Richtung.OBEN;
		leben = maxLeben;
	}

	@Override
	public boolean angriff() {
		if (Spieler.gibInstanz().getPos().dist(pos) <= Spiel.feldLaenge * 1.33) {
			drehung = 360;
			Spieler.gibInstanz().schaden(schaden);
			return true;
		}

		return false;
	}

	@Override
	public void beimTod() {
		if (Spieler.gibInstanz().gibAnzahlMuenzen() != Spieler.gibInstanz().zielMuenzen - 1
				&& !Spiel.gibInstanz().getLevel().equals(Level.getLevel(3))) {
			int r = Utils.random(100);
			Vektor v = pos.kopieren().round();
			if (pos.mag() > 32)
				v = pos.kopieren().div(32).round();

			do {
				int r0 = Utils.random(-1, 1), r1 = Utils.random(-1, 1);

				if (Spiel.gibInstanz().getLevel().isInBounds(v.kopieren().add(new Vektor(r0, r1))))
					v.add(new Vektor(r0, r1));

			} while (Spiel.gibInstanz().getLevel().getFeld(v).isSolide());

			if (r < dropMuenze)
				Spiel.gibInstanz().getLevel().hinzufuegen(new Item(Item.Typ.muenze, v));
			else if (r < dropMuenze + dropHerz)
				Spiel.gibInstanz().getLevel().hinzufuegen(new Item(Item.Typ.herz, v));
		}
	}

	@Override
	public void zeichnen(Graphics2D g) {
		super.aktualisieren();
		if (drehung > 0)
			g.drawImage(bildDrehen(drehBild, Math.toRadians(drehung)), (int) pos.getX(), (int) pos.getY(),
					(int) Spiel.feldLaenge, (int) Spiel.feldLaenge, null);
		else
			g.drawImage(bildDrehen(grafik, Math.toRadians(blick.getWinkel())), (int) pos.getX(), (int) pos.getY(),
					(int) Spiel.feldLaenge, (int) Spiel.feldLaenge, null);
		if (Spiel.gibInstanz().ticks % delayAngriff == 0 && !angriff())
			;
		else if (Spiel.gibInstanz().ticks % delayBewegung == 0 && pos.equals(ziel)) {
			bewegen();
		}

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
		try {
			return operation.filter(grafik, null);
		} catch (Exception e) {
			return grafik;
		}
	}

	private void bewegen() {
		if (pos.equals(ziel) && Spieler.gibInstanz().getPos().dist(pos) > Spiel.feldLaenge && drehung == 0) {
			Vektor v = Spieler.gibInstanz().getPos().kopieren().sub(pos), z = Vektor.nullVektor;

			if (Math.abs(v.getX()) > Math.abs(v.getY()))
				z = new Vektor(Math.signum(v.getX()), 0).mult(Spiel.feldLaenge).add(pos);
			else
				z = new Vektor(0, Math.signum(v.getY())).mult(Spiel.feldLaenge).add(pos);

			blick = Richtung.getRichtung(pos, z);

			if (bewegungMoeglich(z))
				ziel = z;

			if (!ziel.equals(pos))
				ges = ziel.kopieren().sub(pos).normalize().mult(bewegungGeschw / 10);
		}
	}

	private boolean bewegungMoeglich(Vektor v) {
		return !Spieler.gibInstanz().getZiel().equals(v) && !Spiel.gibInstanz().getLevel().istGegner(v, this);
	}

	public Vektor getZiel() {
		return ziel;
	}

}
