package matrizen.model.gegner;

import static matrizen.core.DateiManager.werte;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import matrizen.core.DateiManager;
import matrizen.core.Utils;
import matrizen.core.DateiManager.Bild;
import matrizen.core.Vektor;
import matrizen.model.Level;
import matrizen.model.Spiel;
import matrizen.model.elemente.Gegner;
import matrizen.model.elemente.Geschoss;
import matrizen.model.elemente.Geschoss.Typ;
import matrizen.model.elemente.Item;
import matrizen.model.elemente.Spieler;

public class DracheGegner extends Gegner {
	public final int maxLeben = werte.get("drache_leben"), schaden = werte.get("drache_schaden"),
			delayAngriff = werte.get("drache_delay_angriff"), weite = werte.get("drache_weite"),
			partikelGeschw = werte.get("drache_partikel_geschw") / 3, dropMuenze = werte.get("drache_drop_muenze"),
			dropHerz = werte.get("drache_drop_muenze");

	public DracheGegner(Vektor vektor) {
		grafik = DateiManager.laden(Bild.figurDrache);
		leben = 200;
		pos = vektor.mult(32);
	}

	@Override
	public boolean angriff() {
		Spiel.gibInstanz().getLevel().hinzufuegen(new Geschoss(Typ.feuer, schaden, weite, pos,
				Spieler.gibInstanz().getPos().kopieren().sub(pos).normalize().mult(partikelGeschw), this));
		return true;
	}

	@Override
	public void beimTod() {
		if (Spiel.gibInstanz().getLevel().equals(Level.getLevel(1)))
			Spiel.gibInstanz().getLevel().hinzufuegen(
					new Item(Item.Typ.stabDreifach, pos.kopieren().div(Spiel.feldLaenge).add(new Vektor(-1, 0))));

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
		g.drawImage(bildDrehen(grafik), (int) pos.getX(), (int) pos.getY(), 32, 32, null);

		if (delayAngriff != 0 && Spiel.gibInstanz().ticks % delayAngriff == 0)
			angriff();
	}

	private Image bildDrehen(BufferedImage grafik) {
		AffineTransform transform = new AffineTransform();
		transform.rotate(getWinkel() + Math.toRadians(180), grafik.getWidth() / 2, grafik.getHeight() / 2);

		AffineTransformOp operation = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return operation.filter(grafik, null);
	}

	private double getWinkel() {
		Vektor v1 = Spieler.gibInstanz().getPos().kopieren().sub(pos);
		Vektor v2 = new Vektor(0, pos.getY());

		return Math.atan2(v1.getY(), v1.getX()) - Math.atan2(v2.getY(), v2.getX());
		// return -Math.atan2(v1.kreuz(v2), v1.skalar(v2));
	}

	public Vektor getZiel() {
		return pos;
	}

}
