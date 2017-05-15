package matrizen.model.elemente;

import static matrizen.view.SpielFenster.logger;

import java.awt.Graphics2D;
import java.util.logging.Level;

import matrizen.core.Richtung;
import matrizen.core.Vektor;
import matrizen.core.Vektor.Rechenmethode;
import matrizen.model.Spiel;
import matrizen.view.SpielFenster;

public class Spieler extends Figur {
	private static Spieler instanz;
	private Vektor posImFeld;
	private int index, delay = 2;
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
		int x = (int) (posImFeld.getX() * Spiel.feldLaenge), y = (int) (posImFeld.getY() * Spiel.feldLaenge);
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

		logger.log(Level.FINEST, "Cooldown f�r Spieler: " + cooldown.toString());

		if (Spiel.gibInstanz().ticks % 5 == 0)
			index++;
	}

	public void bewegen(Richtung r) {
		if (cooldown[Richtung.getIndex(r)] == 0 && bewegungMoeglich(r)) {
			posImFeld.add(r.vektor);
			pos = new Vektor(posImFeld.getX() * Spiel.feldLaenge, posImFeld.getY() * Spiel.feldLaenge);
			cooldown[Richtung.getIndex(r)] = delay;
		}
	}

	private boolean bewegungMoeglich(Richtung r) {
		Vektor v = posImFeld.add(r.vektor, Rechenmethode.kopieren);
		logger.log(Level.WARNING, v.toString());
		return v.getX() >= 0 && v.getY() >= 0 && v.getX() < Spiel.spalten && v.getY() < Spiel.zeilen
				&& !Spiel.gibInstanz().getLevel().getFeld(v).isSolide();
	}

}
