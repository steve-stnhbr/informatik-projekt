package matrizen.model.elemente;

import java.awt.Graphics2D;

import matrizen.core.DateiManager;
import matrizen.core.Vektor;
import matrizen.model.Levelelement;

public class Geschoss extends Levelelement {
	private Typ t;
	private boolean spieler;
	
	public Geschoss(Typ t, Vektor pos, Vektor v, boolean spieler) {
		this.t = t;
		this.pos = pos;
		this.spieler = spieler;
		grafik = DateiManager.laden(DateiManager.Bild.zufaelligeGrafik(t));
		bes = v;
	}

	@Override
	public void zeichnen(Graphics2D g) {
		super.aktualisieren();
		g.drawImage(grafik, (int) pos.getX(), (int) pos.getY(), null);
	}

	public enum Typ implements GrafikTyp {
		kleinBlau(20),
		kleinOrange(7),
		stern(2);
		
		int radius;
		
		private Typ(int radius) {
			this.radius = radius;
		}
	}
}
