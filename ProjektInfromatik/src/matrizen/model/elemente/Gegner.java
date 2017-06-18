package matrizen.model.elemente;

import matrizen.core.Vektor;
import matrizen.model.gegner.DracheGegner;
import matrizen.model.gegner.HexeGegner;
import matrizen.model.gegner.RitterGegner;

public abstract class Gegner extends Figur {

	public abstract void angriff();

	public static Gegner erstellen(int t, int x, int y) {
		switch (t) {
		case 0:
			return new DracheGegner(new Vektor(x, y));
		case 1:
			return new RitterGegner(new Vektor(x, y));
		case 2:
			return new HexeGegner(new Vektor(x, y), true);
		default:
			return null;
		}
	}
}
