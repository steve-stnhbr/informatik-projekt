package matrizen.model.zauberstaebe;

import matrizen.core.DateiManager;
import matrizen.model.Spiel;
import matrizen.model.Zauberstab;
import matrizen.model.elemente.Geschoss;
import matrizen.model.elemente.Spieler;
import matrizen.model.elemente.Geschoss.Typ;

public class DreifachZauberstab extends Zauberstab {
	private final int delay = DateiManager.werte.get("stab_blitz_angriff_delay"),
			schaden = DateiManager.werte.get("stab_blitz_schaden"), pause = DateiManager.werte.get("stab_blitz_pause"),
			weite = DateiManager.werte.get("stab_blitz_weite"),
			partikelGeschw = DateiManager.werte.get("stab_blitz_partikel_geschw");

	private int count;

	public void aktualisieren() {
		if (count > 0)
			count--;
	}

	@Override
	public void schuss() {
		count = 15;

		if (count % pause == 0)
			Spiel.gibInstanz().getLevel()
					.hinzufuegen(new Geschoss(Typ.kleinBlau, schaden, weite, Spieler.gibInstanz().getPos(),
							Spieler.gibInstanz().getBlick().getVektor().kopieren().normalize().mult(partikelGeschw),
							Spieler.gibInstanz()));
	}

}
