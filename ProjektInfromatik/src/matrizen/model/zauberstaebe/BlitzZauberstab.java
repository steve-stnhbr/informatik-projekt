package matrizen.model.zauberstaebe;

import java.awt.Color;
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
	private static BlitzZauberstab instanz;

	private final int weite = DateiManager.werte.get("stab_blitz_weite"),
			schaden = DateiManager.werte.get("stab_blitz_schaden"),
			delayAngriff = DateiManager.werte.get("stab_blitz_delay"),
			dauer = DateiManager.werte.get("stab_blitz_dauer");

	private int d;
	private List<Geschoss> list;

	private BlitzZauberstab() {
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

	@Override
	public Color getFarbe() {
		return new Color(0xe3d806);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + d;
		result = prime * result + dauer;
		result = prime * result + delayAngriff;
		result = prime * result + ((list == null) ? 0 : list.hashCode());
		result = prime * result + schaden;
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
		BlitzZauberstab other = (BlitzZauberstab) obj;
		if (d != other.d)
			return false;
		if (dauer != other.dauer)
			return false;
		if (delayAngriff != other.delayAngriff)
			return false;
		if (list == null) {
			if (other.list != null)
				return false;
		} else if (!list.equals(other.list))
			return false;
		if (schaden != other.schaden)
			return false;
		if (weite != other.weite)
			return false;
		return true;
	}

	public static BlitzZauberstab gibInstanz() {
		if (instanz == null)
			instanz = new BlitzZauberstab();
		return null;
	}

}
