package matrizen.model.elemente;

import java.awt.Graphics2D;

import matrizen.core.Vektor;

public class Spieler extends Figur {
	private static Spieler instanz;
	
	private Spieler() {
		
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
	
}
