package matrizen.model.zauberstaebe;

import matrizen.model.Spiel;
import matrizen.model.Zauberstab;
import matrizen.model.elemente.Geschoss;
import matrizen.model.elemente.Spieler;
import matrizen.model.elemente.Geschoss.Typ;

public class EinfachStarkerStab extends Zauberstab {

	@Override
	public void schuss() {
		Spiel.gibInstanz().getLevel().hinzufuegen(new Geschoss(Typ.kleinBlau, 25, 0, Spieler.gibInstanz().getPos(),
				Spieler.gibInstanz().getBlick().getVektor().kopieren().mult(2f), true));
	}

}
