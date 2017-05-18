package matrizen.model.elemente;

import static matrizen.view.SpielFenster.logger;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.logging.Level;

import javax.swing.border.StrokeBorder;

import matrizen.core.Richtung;
import matrizen.core.Vektor;
import matrizen.core.Vektor.Rechenmethode;
import matrizen.core.event.BewegungsEvent;
import matrizen.core.event.EventManager;
import matrizen.model.Spiel;
import matrizen.view.SpielFenster;

/**
 * Diese Klasse repräsentiert den Spieler
 * 
 * @author Stefan
 *
 */
public class Spieler extends Figur {
	private static Spieler instanz;
	private Vektor posImFeld;
	private int index, delay = 5;
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
	public void zeichnen(Graphics2D g) {
		int x = (int) (posImFeld.getX() * Spiel.feldLaenge), y = (int) (posImFeld.getY() * Spiel.feldLaenge);
		g.setStroke(new BasicStroke(2));
		g.setColor(Color.black);
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

		if (Spiel.gibInstanz().ticks % 5 == 0)
			index++;
	}

	public void bewegen(Richtung r) {
		if (cooldown[Richtung.getIndex(r)] == 0 && bewegungMoeglich(r)) {
			EventManager.gibInstanz().eventUebergeben(new BewegungsEvent(posImFeld, posImFeld.add(r.vektor), r));
			pos = new Vektor(posImFeld.getX() * Spiel.feldLaenge, posImFeld.getY() * Spiel.feldLaenge);
			cooldown[Richtung.getIndex(r)] = delay;
		}
	}

	/**
	 * Diese Methode überprüft, ob die Bewegung, die der Spieler machen mag,
	 * überhaupt möglich ist
	 * 
	 * @param r
	 * @return
	 */
	private boolean bewegungMoeglich(Richtung r) {
		Vektor v = posImFeld.add(r.vektor, Rechenmethode.kopieren);
		logger.log(Level.FINE, v.toString());
		return v.getX() >= 0 && v.getY() >= 0 && v.getX() < Spiel.spalten && v.getY() < Spiel.zeilen
				&& !Spiel.gibInstanz().getLevel().getFeld(v).isSolide();
	}

}
