package matrizen.core.event;

import matrizen.core.Richtung;
import matrizen.core.Vektor;

public class BewegungsEvent extends Event {
	private Vektor von, nach;
	private Richtung r;
	
	public BewegungsEvent(Vektor von, Vektor nach, Richtung r) {
		super();
		this.von = von;
		this.nach = nach;
		this.r = r;
	}

	public Vektor getVon() {
		return von;
	}

	public void setVon(Vektor von) {
		this.von = von;
	}

	public Vektor getNach() {
		return nach;
	}

	public void setNach(Vektor nach) {
		this.nach = nach;
	}

	public Richtung getRichtung() {
		return r;
	}

	public void setRichtung(Richtung r) {
		this.r = r;
	}
	
}
