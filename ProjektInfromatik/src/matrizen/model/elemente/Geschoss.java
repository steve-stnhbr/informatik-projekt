package matrizen.model.elemente;

import java.awt.Graphics2D;

import matrizen.core.DateiManager;
import matrizen.core.Vektor;
import matrizen.model.Levelelement;

public class Geschoss extends Levelelement {
	private final Vektor start;
	private Typ t;
	private boolean spieler, aktiv = true;
	private int schaden, weite;

	public Geschoss(Typ t, int schaden, int weite, Vektor pos, Vektor v, boolean spieler) {
		this.t = t;
		this.schaden = schaden;
		this.weite = weite;
		this.pos = pos;
		this.spieler = spieler;
		grafik = DateiManager.laden(DateiManager.Bild.zufaelligeGrafik(t));
		bes = v;

		start = pos;
	}

	@Override
	public void zeichnen(Graphics2D g) {
		super.aktualisieren();
		if (aktiv)
			g.drawImage(grafik, (int) pos.getX(), (int) pos.getY(), null);

		if (start.dist(pos) >= weite && weite != 0)
			aktiv = false;
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

	public boolean isAktiv() {
		return aktiv;
	}

	public enum Typ implements GrafikTyp {
		kleinBlau(10), kleinOrange(10), stern(5), feuer(7);

		int radius;

		private Typ(int radius) {
			this.radius = radius;
		}

		public float getRadius() {
			return radius;
		}
	}
}
