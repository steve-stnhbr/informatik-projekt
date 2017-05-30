package matrizen.model;

import java.awt.Graphics2D;

import matrizen.core.Vektor;

public abstract class Beweglich extends Grafisch {
	protected Vektor pos = Vektor.nullVektor, ges = Vektor.nullVektor, bes= Vektor.nullVektor;
	
	public void zeichnen(Graphics2D g) {
		aktualisieren();
	}
	
	public void kraftAusueben(Vektor v) {
		bes.add(v);
	}
	
	public void aktualisieren() {
		pos = pos.kopieren().add(ges);
		ges = ges.kopieren().add(bes);
		bes.mult(0f);
	}
}
