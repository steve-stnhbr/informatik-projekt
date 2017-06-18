package matrizen.model.gegner;

import java.awt.Graphics2D;

import matrizen.core.Vektor;
import matrizen.model.Levelelement;

public class HexeGegner extends Levelelement {
	private boolean aktiv;

	public HexeGegner(Vektor feldPos, boolean aktiv) {

	}

	@Override
	public void zeichnen(Graphics2D g) {

	}

	public boolean isAktiv() {
		return aktiv;
	}

}
