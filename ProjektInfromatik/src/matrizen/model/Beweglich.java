package matrizen.model;

import matrizen.core.Vektor;

public interface Beweglich extends Grafisch {
	
	public void kraftAusueben(Vektor v);
	public void aktualisieren();
}
