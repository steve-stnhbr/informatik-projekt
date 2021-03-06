package matrizen.model.zauberstaebe;

import java.awt.Color;

import matrizen.core.DateiManager;
import matrizen.model.Spiel;
import matrizen.model.Zauberstab;
import matrizen.model.elemente.Geschoss;
import matrizen.model.elemente.Spieler;
import matrizen.model.elemente.Geschoss.Typ;

public class MehrfachZauberstab extends Zauberstab {
	private static MehrfachZauberstab instanz;

	private final int delayAngriff = DateiManager.werte.get("stab_mehr_angriff_delay"),
			schaden = DateiManager.werte.get("stab_mehr_schaden"), pause = DateiManager.werte.get("stab_mehr_pause"),
			weite = DateiManager.werte.get("stab_mehr_weite"),
			partikelGeschw = DateiManager.werte.get("stab_mehr_partikel_geschw"),
			anzahlPartikel = DateiManager.werte.get("stab_mehr_partikel_anzahl");

	private int count;

	private MehrfachZauberstab() {
		delay = delayAngriff;
	}

	public void aktualisieren() {
		if (count > 0 && count % pause == 0)
			Spiel.gibInstanz().getLevel()
					.hinzufuegen(new Geschoss(
							Typ.kleinBlau, schaden, weite, Spieler.gibInstanz().getPos(), Spieler.gibInstanz()
									.getBlick().getVektor().kopieren().normalize().mult(partikelGeschw / 10),
							Spieler.gibInstanz()));
		if (count > 0)
			count--;
	}

	@Override
	public void schuss() {
		count = pause * anzahlPartikel;
	}

	@Override
	public Color getFarbe() {
		return new Color(0x006fd7);
	}

	public static MehrfachZauberstab gibInstanz() {
		if (instanz == null)
			instanz = new MehrfachZauberstab();
		return instanz;
	}

}
