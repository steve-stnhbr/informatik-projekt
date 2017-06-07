package matrizen.model.elemente;

import java.awt.Graphics2D;

import matrizen.core.DateiManager;
import matrizen.core.Vektor;
import matrizen.model.Levelelement;

public class Geschoss extends Levelelement {
	private Typ t;
	private boolean spieler;
	private int schaden;
	
	public Geschoss(Typ t, int schaden, Vektor pos, Vektor v, boolean spieler) {
		this.t = t;
		this.schaden = schaden;
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

	public Typ getTyp() {
		return t;
	}

	public boolean isSpieler() {
		return spieler;
	}

	public int getSchaden() {
		return schaden;
	}

	public enum Typ implements GrafikTyp {
		kleinBlau(20),
		kleinOrange(7),
		stern(5);
		
		int radius;
		
		private Typ(int radius) {
			this.radius = radius;
		}

		public float getRadius() {
			return radius;
		}
	}
}
