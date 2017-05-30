package matrizen.model.elemente;

import java.awt.Graphics2D;

import matrizen.core.Vektor;
import matrizen.model.Gegenstand;

public class Geschoss extends Gegenstand {
	private Typ t;
	private boolean spieler;
	
	public Geschoss(Typ t, boolean spieler) {
		this.t = t;
		this.spieler = spieler;
	}

	@Override
	public void zeichnen(Graphics2D g) {
		g.drawImage(grafik, (int) pos.getX(), (int) pos.getY(), null);
	}

	public enum Typ implements GrafikTyp {
		kleinBlau(20),
		kleinOrange(7);
		
		int radius;
		
		private Typ(int radius) {
			this.radius = radius;
		}
	}
}
