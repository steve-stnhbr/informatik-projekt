package matrizen.model.elemente;

import java.awt.Graphics2D;

import matrizen.core.DateiManager;
import matrizen.core.Utils;
import matrizen.core.Vektor;
import matrizen.model.Spiel;

public class VerfolgerGeschoss extends Geschoss {
	private final int schadenNormal = DateiManager.werte.get("stab_folgen_schaden_normal"),
			schadenVerfolgung = DateiManager.werte.get("stab_folgen_schaden_verf"),
			geschw = DateiManager.werte.get("stab_folgen_geschw"),
			anziehung = DateiManager.werte.get("stab_folgen_anziehung");

	private Figur ziel;
	private int weg;
	private Vektor posDavor;

	private VerfolgerGeschoss(Typ t, int schaden, int weite, Vektor pos, Vektor v, Figur schuetze) {
		super(t, schaden, weite, pos, v, schuetze);
	}

	public VerfolgerGeschoss(Vektor pos, Vektor v, Figur ziel, Figur schuetze, int weite) {
		super(Typ.verfolger, 0, weite, pos, v, schuetze);
		this.weite = weite;
		this.ziel = ziel;
		System.out.println(ziel);
		posDavor = pos;
	}

	@Override
	public void zeichnen(Graphics2D g) {
		super.aktualisieren();
		super.zeichnen(g);
		weg += pos.dist(posDavor);
		if (ziel != null)
			bes.add(ziel.getPos().kopieren().sub(pos).normalize().mult(Utils.begrenzen(pos.dist(ziel.getPos()), -3, 3)));
		ges.normalize().mult(geschw / 10);
		if (weite != 0 && weg >= weite)
			Spiel.gibInstanz().getLevel().entfernen(this);
		posDavor = pos;
	}

	@Override
	public int getSchaden() {
		return ziel == null ? schadenNormal : schadenVerfolgung;
	}

}
