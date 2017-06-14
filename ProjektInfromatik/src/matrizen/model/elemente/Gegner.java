package matrizen.model.elemente;

import java.awt.Graphics2D;

import matrizen.core.Vektor;
import matrizen.model.Levelelement;
import matrizen.model.gegner.StatueGegner;
import matrizen.model.gegner.TestGegner;

public abstract class Gegner extends Figur {

	public abstract void angriff();

	public static Gegner erstellen(int t, int x, int y) {
		switch(t) {
		case 0: return new TestGegner(new Vektor(x, y));
		case 1: return new StatueGegner(new Vektor(x, y));
		default: return null;
		}
	}
}
