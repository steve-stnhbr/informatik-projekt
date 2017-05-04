package matrizen.model;

import matrizen.core.Vektor;

public abstract class Bewegbar extends Grafikobjekt {
	private Vektor pos, ges, bes;
	
	public void aktualisieren() {
		pos.add(ges);
		ges.add(bes);
	}
}
