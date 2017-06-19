package matrizen.model.zauberstaebe;

import matrizen.model.Spiel;
import matrizen.model.Zauberstab;
import matrizen.model.elemente.Spieler;
import matrizen.model.elemente.VerfolgerGeschoss;

public class VerfolgungsZauberstab extends Zauberstab {

	@Override
	public void schuss() {
		Spiel.gibInstanz().getLevel()
				.hinzufuegen(new VerfolgerGeschoss(Spieler.gibInstanz().getPos(),
						Spieler.gibInstanz().getBlick().getVektor(),
						Spiel.gibInstanz().getLevel().gibNaechstenGegner(Spieler.gibInstanz().getPos()),
						Spieler.gibInstanz(), schaden, reichweite));
	}

}
