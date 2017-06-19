package matrizen.model.zauberstaebe;

import java.util.ArrayList;
import java.util.List;

import matrizen.core.DateiManager;
import matrizen.core.Utils;
import matrizen.core.Vektor;
import matrizen.model.Spiel;
import matrizen.model.Zauberstab;
import matrizen.model.elemente.Geschoss;
import matrizen.model.elemente.Geschoss.Typ;
import matrizen.model.elemente.Spieler;

public class BlitzZauberstab extends Zauberstab {
	private final int weite = DateiManager.werte.get("stab_blitz_weite"),
			schaden = DateiManager.werte.get("stab_blitz_schaden"),
			delayAngriff = DateiManager.werte.get("stab_blitz_delay"),
			dauer = DateiManager.werte.get("stab_blitz_dauer");

	private int d;
	private List<Geschoss> list;

	public BlitzZauberstab() {
		delay = delayAngriff;
		list = new ArrayList<>();
	}

	@Override
	public void schuss() {
		setzeBlitz();
		d = dauer;
	}

	@Override
	public void aktualisieren() {
		if (d > 0)
			d--;

		if (d == 0)
			entferneBlitz();
	}

	private void setzeBlitz() {
		Vektor p = Spieler.gibInstanz().getPos().kopieren().div(32).round();
		int i = weite == 0 ? 1000 : weite;

		do {
			p.add(Spieler.gibInstanz().getBlick().getFinalVektor()).round();
			Geschoss g = new Geschoss(
					Spieler.gibInstanz().getBlick().getVektor().getY() == 0 ? Typ.blitzHorizontal : Typ.blitzVertikal,
					schaden, 0, p.kopieren().mult(32), Vektor.nullVektor, Spieler.gibInstanz());
			list.add(g);
			Spiel.gibInstanz().getLevel().hinzufuegen(g);
			i--;
		} while (Utils.vektorIstInFeld(p) && i > 0);

	}

	private void entferneBlitz() {
		Spiel.gibInstanz().getLevel().alleEntfernen(list);
		list.removeAll(list);
	}

}
