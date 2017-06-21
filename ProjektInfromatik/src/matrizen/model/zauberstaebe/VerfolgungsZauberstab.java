package matrizen.model.zauberstaebe;

import java.awt.Color;

import matrizen.core.DateiManager;
import matrizen.model.Spiel;
import matrizen.model.Zauberstab;
import matrizen.model.elemente.Spieler;
import matrizen.model.elemente.VerfolgerGeschoss;

public class VerfolgungsZauberstab extends Zauberstab {
	private static VerfolgungsZauberstab instanz;
	private final int weite = DateiManager.werte.get("stab_folgen_weite");

	private VerfolgungsZauberstab() {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + weite;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VerfolgungsZauberstab other = (VerfolgungsZauberstab) obj;
		if (weite != other.weite)
			return false;
		return true;
	}

	public static VerfolgungsZauberstab gibInstanz() {
		if (instanz == null)
			instanz = new VerfolgungsZauberstab();
		return instanz;
	}
}
