package matrizen.model.zauberstaebe;

import static matrizen.core.DateiManager.werte;

import matrizen.model.Spiel;
import matrizen.model.Zauberstab;
import matrizen.model.elemente.Geschoss;
import matrizen.model.elemente.Spieler;

public class EinfachSchwacherStab extends Zauberstab {

	public EinfachSchwacherStab() {
		delay = werte.get("es_angriff");
		schaden = werte.get("es_schaden");
		reichweite = werte.get("es_weite");
		geschw = werte.get("es_geschw");
	}

	@Override
	public void schuss() {
		Spiel.gibInstanz().getLevel()
				.hinzufuegen(new Geschoss(Geschoss.Typ.stern, schaden, reichweite, Spieler.gibInstanz().getPos(),
						Spieler.gibInstanz().getBlick().getVektor().kopieren().mult(geschw), true));
	}

}
