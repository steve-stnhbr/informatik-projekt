package matrizen.model;

import matrizen.core.Vektor;

public abstract class Beweglich extends Grafisch {
	protected Vektor pos = Vektor.nullVektor, ges = Vektor.nullVektor, bes= Vektor.nullVektor;
	
	public void kraftAusueben(Vektor v) {
		bes.add(v);
	}
	
	public void aktualisieren() {
		pos.add(ges);
		ges.add(bes);
	}
}
