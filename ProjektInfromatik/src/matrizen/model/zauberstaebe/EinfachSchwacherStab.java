package matrizen.model.zauberstaebe;

import static matrizen.core.DateiManager.werte;

import matrizen.model.Spiel;
import matrizen.model.Zauberstab;
import matrizen.model.elemente.Geschoss;
import matrizen.model.elemente.Spieler;

public class EinfachSchwacherStab extends Zauberstab {

	public EinfachSchwacherStab() {
		delay = werte.get("stab_einfach_delay_angriff");
		schaden = werte.get("stab_einfach_schaden");
		reichweite = werte.get("stab_einfach_weite");
		geschw = werte.get("stab_einfach_partikel_geschw");
	}

	@Override
	public void schuss() {
		Spiel.gibInstanz().getLevel()
				.hinzufuegen(new Geschoss(Geschoss.Typ.kleinBlau, schaden, reichweite, Spieler.gibInstanz().getPos(),
						Spieler.gibInstanz().getBlick().getVektor().kopieren().mult(geschw), Spieler.gibInstanz()));
	}

}
