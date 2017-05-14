package matrizen.model.elemente;

import java.awt.Graphics2D;

import matrizen.core.Richtung;
import matrizen.core.Vektor;
import matrizen.model.Spiel;
import matrizen.view.SpielFenster;

public class Spieler extends Figur {
	private static Spieler instanz;
	private Vektor posImFeld;
	
	private Spieler() {
		//lööl
	}
	
	public static Spieler gibInstanz() {
		if(instanz == null)
			instanz = new Spieler();
		return instanz;
	}

	@Override
	public void kraftAusueben(Vektor v) {
		
	}

	@Override
	public void zeichnen(Graphics2D g) {
		
	}
	
	public void bewegen(Richtung r) {
		posImFeld.add(r.vektor);
		pos = posImFeld.mult(SpielFenster.hoehe / Spiel.spalten);
	}
	
}
