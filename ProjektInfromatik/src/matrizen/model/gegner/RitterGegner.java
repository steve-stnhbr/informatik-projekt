package matrizen.model.gegner;

import static matrizen.core.DateiManager.werte;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import matrizen.core.DateiManager;
import matrizen.core.DateiManager.Bild;
import matrizen.core.Vektor;
import matrizen.model.Spiel;
import matrizen.model.elemente.Gegner;
import matrizen.model.elemente.Spieler;

public class RitterGegner extends Gegner {
	public final int maxLeben = werte.get("ritter_leben"), delayAngriff = werte.get("ritter_delay_angriff"),
			drehGeschw = werte.get("ritter_drehung"), schaden = werte.get("ritter_schaden"),
			delayBewegung = werte.get("ritter_delay_bewegung"), geschw = werte.get("ritter_bewegung_geschw");
	private int drehung;
	private Vektor ziel;

	public RitterGegner(Vektor feldPos) {
		pos = feldPos.mult(32);
		grafik = DateiManager.laden(Bild.figurRitter);
		animation = new BufferedImage[] { DateiManager.laden(Bild.figurRitterDrehung) };
		ziel = pos;
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
			g.drawImage(animation[0], (int) pos.getX(), (int) pos.getY(), 32, 32, null);
		else
			g.drawImage(grafik, (int) pos.getX(), (int) pos.getY(), 32, 32, null);

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

	private void bewegen() {
		if (pos.equals(ziel) && Spieler.gibInstanz().getPos().dist(pos) > 32) {
			Vektor z = Spieler.gibInstanz().getPos().kopieren().sub(pos);

			if (Math.abs(z.getX()) > Math.abs(z.getY()))
				ziel = new Vektor(Math.signum(z.getX()), 0).mult(32).add(pos);
			else
				ziel = new Vektor(0, Math.signum(z.getY())).mult(32).add(pos);

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
