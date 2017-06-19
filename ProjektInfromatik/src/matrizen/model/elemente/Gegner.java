package matrizen.model.elemente;

import matrizen.core.Vektor;
import matrizen.model.gegner.DracheGegner;
import matrizen.model.gegner.HexeGegner;
import matrizen.model.gegner.RitterGegner;
import matrizen.model.gegner.ZombieGegner;

public abstract class Gegner extends Figur {

	public abstract boolean angriff();
	public abstract Vektor getZiel();

	public static Gegner erstellen(int t, int x, int y) {
		switch (t) {
		case 0:
			return new DracheGegner(new Vektor(x, y));
		case 1:
			return new RitterGegner(new Vektor(x, y));
		case 2:
			return new HexeGegner(new Vektor(x, y), true);
		case 3:
			return new ZombieGegner(new Vektor(x, y));
		default:
			return null;
		}
	}
}
