package matrizen.model.elemente;

import java.awt.Graphics2D;

import matrizen.core.Richtung;
import matrizen.core.Vektor;
import matrizen.model.Spiel;
import matrizen.view.SpielFenster;

public class Spieler extends Figur {
	private static Spieler instanz;
	private Vektor posImFeld;
	private int index;

	private Spieler() {}

	public static Spieler gibInstanz() {
		if (instanz == null)
			instanz = new Spieler();
		return instanz;
	}

	@Override
	public void kraftAusueben(Vektor v) {

	}

	@Override
	public void zeichnen(Graphics2D g) {
		if (ges.mag() < .5)
			g.drawImage(grafik, (int) (posImFeld.getX() * Spiel.feldLaenge),
					(int) (posImFeld.getY() * Spiel.feldLaenge), (int) Spiel.feldLaenge, (int) Spiel.feldLaenge, null);
		else
			g.drawImage(animation[index], (int) (posImFeld.getX() * Spiel.feldLaenge),
					(int) (posImFeld.getY() * Spiel.feldLaenge), (int) Spiel.feldLaenge, (int) Spiel.feldLaenge, null);
		if (Spiel.gibInstanz().ticks % 5 == 0)
			index++;
	}

	public void bewegen(Richtung r) {
		posImFeld.add(r.vektor);
		pos = posImFeld.mult(SpielFenster.hoehe / Spiel.spalten);
	}

}
