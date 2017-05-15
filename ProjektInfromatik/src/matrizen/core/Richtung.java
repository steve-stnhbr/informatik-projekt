package matrizen.core;

public enum Richtung {
	OBEN(new Vektor(0, -1), 0),
	UNTEN(new Vektor(0, 1), 180),
	RECHTS(new Vektor(1, 0), 90),
	LINKS(new Vektor(-1, 0), 270);

	public Vektor vektor;
	public int winkel;

	private Richtung(Vektor v, int w) {
		this.vektor = v;
		this.winkel = w;
	}
	
	public static Richtung getRichtung(int index) {
		return values()[index];
	}
	
	public static int getIndex(Richtung r) {
		for(int i = 0; i < values().length; i++) 
			if(values()[i] == r) return i;
		return -1;
	}
}
