package matrizen.model.elemente;

import java.awt.Graphics2D;

import matrizen.core.DateiManager;
import matrizen.core.Vektor;
import matrizen.model.Levelelement;
import matrizen.model.Spiel;

public class Geschoss extends Levelelement {
	private final Vektor start;
	private Typ t;
	private boolean spieler, aktiv = true;
	private int schaden, weite;

	public Geschoss(Typ t, int schaden, int weite, Vektor pos, Vektor v, Figur schuetze) {
		this.t = t;
		this.schaden = schaden;
		this.weite = weite;
		this.pos = pos;
		this.spieler = schuetze.equals(Spieler.gibInstanz());
		grafik = DateiManager.laden(DateiManager.Bild.zufaelligeGrafik(t));
		bes = v;

		start = pos;
	}

	@Override
	public void zeichnen(Graphics2D g) {
		super.aktualisieren();
		if (aktiv)
			g.drawImage(grafik, (int) pos.getX(), (int) pos.getY(), Spiel.feldLaenge, Spiel.feldLaenge, null);

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (aktiv ? 1231 : 1237);
		result = prime * result + schaden;
		result = prime * result + (spieler ? 1231 : 1237);
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		result = prime * result + ((t == null) ? 0 : t.hashCode());
		result = prime * result + weite;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Geschoss other = (Geschoss) obj;
		if (aktiv != other.aktiv)
			return false;
		if (schaden != other.schaden)
			return false;
		if (spieler != other.spieler)
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		if (t != other.t)
			return false;
		if (weite != other.weite)
			return false;
		return true;
	}

	public enum Typ implements GrafikTyp {
		kleinBlau(10),
		kleinOrange(10),
		stern(5),
		feuer(7),
		verfolger(7),
		raute(7),
		blitzHorizontal(5),
		blitzVertikal(5);

		int radius;

		private Typ(int radius) {
			this.radius = radius;
		}

		public float getRadius() {
			return radius;
		}
	}
}
