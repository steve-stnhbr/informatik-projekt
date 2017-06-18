package matrizen.model.zauberstaebe;

import matrizen.core.DateiManager;
import matrizen.model.Spiel;
import matrizen.model.Zauberstab;
import matrizen.model.elemente.Geschoss;
import matrizen.model.elemente.Spieler;
import matrizen.model.elemente.Geschoss.Typ;

public class EinfachStarkerStab extends Zauberstab {
	private final int geschw = DateiManager.werte.get(""), schaden = DateiManager.werte.get(""),
			reichweite = DateiManager.werte.get("");

	@Override
	public void schuss() {
		Spiel.gibInstanz().getLevel().hinzufuegen(new Geschoss(Typ.kleinBlau, schaden, 0, Spieler.gibInstanz().getPos(),
				Spieler.gibInstanz().getBlick().getVektor().kopieren().mult(2f), Spieler.gibInstanz()));
	}

}
