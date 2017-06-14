package matrizen.model.gegner;

import static matrizen.core.DateiManager.werte;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import matrizen.core.DateiManager;
import matrizen.core.DateiManager.Bild;
import matrizen.core.Vektor;
import matrizen.model.Spiel;
import matrizen.model.elemente.Gegner;
import matrizen.model.elemente.Geschoss;
import matrizen.model.elemente.Geschoss.Typ;
import matrizen.model.elemente.Spieler;

public class StatueGegner extends Gegner {
	public final int maxLeben = werte.get("drache_leben"), schaden = werte.get("drache_schaden"),
			delayAngriff = werte.get("drache_angriff"), weite = werte.get("drache_weite");

	public StatueGegner(Vektor vektor) {
		grafik = DateiManager.laden(Bild.figurGegener);
		leben = 200;
		pos = vektor.mult(32);
	}

	@Override
	public void angriff() {
		Spiel.gibInstanz().getLevel().hinzufuegen(new Geschoss(Typ.feuer, schaden, weite, pos,
				pos.kopieren().sub(Spieler.gibInstanz().getPos()).mult(7f), false));
	}

	@Override
	public void beimTod() {

	}

	@Override
	public void zeichnen(Graphics2D g) {
		g.drawImage(grafik, (int) pos.getX(), (int) pos.getY(), 32, 32, null);

		if (Spiel.gibInstanz().ticks % delayAngriff == 0)
			angriff();
	}

	// TODO
	private Image bildDrehen(BufferedImage grafik) {
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(Math.asin(Spieler.gibInstanz().getPos().kopieren().sub(pos).skalar(pos))),
				grafik.getWidth() / 2, grafik.getHeight() / 2);

		AffineTransformOp operation = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);

		return operation.filter(grafik, null);
	}

}
