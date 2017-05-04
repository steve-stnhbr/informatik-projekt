package matrizen.model;

import matrizen.core.Vektor;

public abstract class Beweglich extends Grafisch {
	protected Vektor pos, ges, bes;
	
	public abstract void kraftAusueben(Vektor v);
	
	public void aktualisieren() {
		pos.add(ges);
		ges.add(bes);
	}
}
