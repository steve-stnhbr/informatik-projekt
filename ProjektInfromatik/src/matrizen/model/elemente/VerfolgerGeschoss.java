package matrizen.model.elemente;

import java.awt.Graphics2D;

import matrizen.core.Vektor;

public class VerfolgerGeschoss extends Geschoss {

	private Figur ziel;

	private VerfolgerGeschoss(Typ t, int schaden, int weite, Vektor pos, Vektor v, Figur schuetze) {
		super(t, schaden, weite, pos, v, schuetze);
	}

	public VerfolgerGeschoss(Vektor pos, Vektor v, Figur ziel, Figur schuetze, int schaden, int weite) {
		super(Typ.verfolger, schaden, weite, pos, v, schuetze);
		this.ziel = ziel;
	}

	@Override
	public void zeichnen(Graphics2D g) {
		super.zeichnen(g);

		if (ziel != null)
			bes.add(pos.kopieren().sub(ziel.getPos()));
	}

}
