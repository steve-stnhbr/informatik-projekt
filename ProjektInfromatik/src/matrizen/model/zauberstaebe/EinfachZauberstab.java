package matrizen.model.zauberstaebe;

import static matrizen.core.DateiManager.werte;

import java.awt.Color;

import matrizen.model.Spiel;
import matrizen.model.Zauberstab;
import matrizen.model.elemente.Geschoss;
import matrizen.model.elemente.Spieler;

public class EinfachZauberstab extends Zauberstab {

	public EinfachZauberstab() {
		delay = werte.get("stab_einfach_delay_angriff");
		schaden = werte.get("stab_einfach_schaden");
		reichweite = werte.get("stab_einfach_weite");
		geschw = werte.get("stab_einfach_partikel_geschw");
	}

	@Override
	public void schuss() {
		Spiel.gibInstanz().getLevel()
				.hinzufuegen(new Geschoss(Geschoss.Typ.kleinBlau, schaden, reichweite, Spieler.gibInstanz().getPos(),
						Spieler.gibInstanz().getBlick().getVektor().kopieren().normalize().mult(geschw / 10), Spieler.gibInstanz()));
	}

	@Override
	public Color getFarbe() {
		return new Color(0xd70000);
	}

}
