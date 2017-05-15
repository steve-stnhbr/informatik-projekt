package matrizen.model.elemente;

import static matrizen.view.SpielFenster.logger;

import java.awt.Graphics2D;
import java.util.logging.Level;

import matrizen.core.Richtung;
import matrizen.core.Vektor;
import matrizen.model.Spiel;
import matrizen.view.SpielFenster;

public class Spieler extends Figur {
	private static Spieler instanz;
	private Vektor posImFeld;
	private int index, delay = 10;
	private int[] cooldown;

	private Spieler() {
		posImFeld = Vektor.nullVektor;
		cooldown = new int[5];
	}

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
		int x = (int) (posImFeld.getX() * Spiel.feldLaenge),
				y = (int) ((posImFeld.getY() + 1) * Spiel.feldLaenge);
		g.drawRect(x, y, (int) Spiel.feldLaenge, (int) Spiel.feldLaenge);
		/*
		 * if (ges.mag() < .5) g.drawImage(grafik, (int) (posImFeld.getX() *
		 * Spiel.feldLaenge), (int) (posImFeld.getY() * Spiel.feldLaenge), (int)
		 * Spiel.feldLaenge, (int) Spiel.feldLaenge, null); else
		 * g.drawImage(animation[index], (int) (posImFeld.getX() *
		 * Spiel.feldLaenge), (int) (posImFeld.getY() * Spiel.feldLaenge), (int)
		 * Spiel.feldLaenge, (int) Spiel.feldLaenge, null);
		 */
		for (int i = 0; i < cooldown.length; i++) {
			int c = cooldown[i];
			if (c > 0)
				cooldown[i] = c - 1;
		}

		logger.log(Level.FINEST, "Cooldown für Spieler: " + cooldown);

		if (Spiel.gibInstanz().ticks % 5 == 0)
			index++;
	}

	public void bewegen(Richtung r) {
		if (cooldown[Richtung.getIndex(r)] == 0) {
			logger.log(Level.WARNING, "Spieler auf Position " + posImFeld + " bewegt");
			posImFeld.add(r.vektor);
			pos = new Vektor(posImFeld.getX() * Spiel.feldLaenge, posImFeld.getY() * Spiel.feldLaenge);
			cooldown[Richtung.getIndex(r)] = delay;
		}
	}

}
