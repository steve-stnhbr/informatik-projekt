package matrizen.core;

public enum Richtung {
	OBEN(new Vektor(0, -1), 0),
	UNTEN(new Vektor(0, 1), 180),
	RECHTS(new Vektor(1, 0), 90),
	LINKS(new Vektor(-1, 0), 270);

	private Vektor vektor;
	private int winkel;

	private Richtung(Vektor v, int w) {
		this.vektor = v;
		this.winkel = w;
	}

	public static Richtung getRichtung(int index) {
		return values()[index];
	}

	public Vektor getVektor() {
		return vektor;
	}

	public int getWinkel() {
		return winkel;
	}

	public Vektor getFinalVektor() {
		switch (this) {
		case OBEN:
			return new Vektor(0, -1);
		case UNTEN:
			return new Vektor(0, 1);
		case LINKS:
			return new Vektor(-1, 0);
		case RECHTS:
			return new Vektor(1, 0);
		default:
			return Vektor.nullVektor;
		}
	}

	public static int getIndex(Richtung r) {
		for (int i = 0; i < values().length; i++)
			if (values()[i] == r)
				return i;
		return -1;
	}

	public static Richtung getRichtung(Vektor start, Vektor ziel) {
		Vektor v = ziel.kopieren().sub(start);
		v.normalize();

		/*
		 * for (Richtung r : values()) { if (v.equals(r.getFinalVektor()))
		 * return r; }
		 */

		System.out.println(v);
		if (v.getX() == 1 && v.getY() == 0)
			return RECHTS;
		else if (v.getX() == -1 && v.getY() == 0)
			return LINKS;
		else if (v.getY() == 1 && v.getX() == 0)
			return UNTEN;
		else if (v.getY() == -1 && v.getX() == 0)
			return OBEN;
		return Richtung.OBEN;
	}
}
