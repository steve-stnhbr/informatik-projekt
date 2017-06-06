package matrizen.model;

import matrizen.core.Vektor;

public abstract class Beweglich extends Grafisch {
	protected Vektor pos = Vektor.nullVektor, ges = Vektor.nullVektor, bes = Vektor.nullVektor;

	public void kraftAusueben(Vektor v) {
		bes = bes.add(v);
	}

	public void aktualisieren() {
		ges = ges.kopieren().add(bes);
		pos = pos.kopieren().add(ges);
		bes = bes.kopieren().mult(0);
	}

	public Vektor getPos() {
		return pos;
	}

	public void setPos(Vektor pos) {
		this.pos = pos;
	}

	public Vektor getGes() {
		return ges;
	}

	public void setGes(Vektor ges) {
		this.ges = ges;
	}

	public Vektor getBes() {
		return bes;
	}

	public void setBes(Vektor bes) {
		this.bes = bes;
	}
}
