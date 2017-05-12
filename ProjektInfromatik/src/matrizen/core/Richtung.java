package matrizen.core;

public enum Richtung {
	OBEN(new Vektor(0, 1), 0), UNTEN(new Vektor(0, -1), 180), RECHTS(new Vektor(1, 0), 90), LINKS(new Vektor(-1, 0),
			270);

	public Vektor vektor;
	public int winkel;

	private Richtung(Vektor v, int w) {
		this.vektor = v;
		this.winkel = w;
	}
}
