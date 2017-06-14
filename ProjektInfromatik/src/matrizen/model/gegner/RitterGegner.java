package matrizen.model.gegner;

import static matrizen.core.DateiManager.werte;

import java.awt.Graphics2D;

import matrizen.core.DateiManager;
import matrizen.model.Spiel;
import matrizen.model.elemente.Gegner;
import matrizen.model.elemente.Spieler;

public class RitterGegner extends Gegner {
	public final int maxLeben = werte.get("ritter_leben"), angriff = werte.get("ritter_angriff"),
			drehGeschw = werte.get("ritter_drehung"), schaden = werte.get("ritter_schaden"),
			bewegung = werte.get("ritter_bewegung");
	private int drehung;

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
		g.drawImage(grafik, (int) pos.getX(), (int) pos.getY(), 32, 32, null);

		if (Spiel.gibInstanz().ticks % bewegung == 0)
			bewegen();
		if (Spiel.gibInstanz().ticks % angriff == 0)
			angriff();

		if (drehung > 0)
			drehung -= drehGeschw;
		if (drehung < 0)
			drehung = 0;
	}

	private void bewegen() {

	}

}
