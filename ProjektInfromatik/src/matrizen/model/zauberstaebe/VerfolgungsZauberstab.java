package matrizen.model.zauberstaebe;

import java.awt.Color;

import matrizen.core.DateiManager;
import matrizen.model.Spiel;
import matrizen.model.Zauberstab;
import matrizen.model.elemente.Spieler;
import matrizen.model.elemente.VerfolgerGeschoss;

public class VerfolgungsZauberstab extends Zauberstab {
	private final int weite = DateiManager.werte.get("stab_folgen_weite");

	public VerfolgungsZauberstab() {
		delay = DateiManager.werte.get("stab_folgen_delay");
	}

	@Override
	public void schuss() {
		Spiel.gibInstanz().getLevel()
				.hinzufuegen(new VerfolgerGeschoss(Spieler.gibInstanz().getPos(),
						Spieler.gibInstanz().getBlick().getVektor(),
						Spiel.gibInstanz().getLevel().gibNaechstenGegner(Spieler.gibInstanz().getPos()),
						Spieler.gibInstanz(), weite));
	}

	@Override
	public Color getFarbe() {
		return new Color(0xe18608);
	}

}
