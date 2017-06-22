package matrizen.model.elemente;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;

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
		posDavor = pos;
		bes = Spieler.gibInstanz().getBlick().getFinalVektor().kopieren().mult(geschw / 2.5f);
	}

	@Override
	public void zeichnen(Graphics2D g) {
		super.aktualisieren();

		g.drawImage(bildDrehen(grafik), (int) pos.getX(), (int) pos.getY(), Spiel.feldLaenge, Spiel.feldLaenge, null);

		weg += pos.dist(posDavor);
		if (ziel != null)
			bes.add(ziel.getPos().kopieren().sub(pos).normalize().mult(Utils.begrenzen(pos.dist(ziel.getPos()), -3, 3))
					.mult(anziehung / 10));
		ges.normalize().mult(geschw / 10);
		if (weite != 0 && weg >= weite)
			Spiel.gibInstanz().getLevel().entfernen(this);
		posDavor = pos;
	}

	private Image bildDrehen(BufferedImage grafik) {
		AffineTransform transform = new AffineTransform();
		transform.rotate(getWinkel() + Math.PI, grafik.getWidth() / 2, grafik.getHeight() / 2);

		AffineTransformOp operation = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		try {
			return operation.filter(grafik, null);
		} catch(RasterFormatException e) {
			return grafik;
		}
	}

	private double getWinkel() {
		Vektor v1 = ges.kopieren().add(pos);
		Vektor v2 = new Vektor(0, pos.getY());

		return Math.atan2(v1.getY(), v1.getX()) - Math.atan2(v2.getY(), v2.getX());
	}

	@Override
	public int getSchaden() {
		return ziel == null ? schadenNormal : schadenVerfolgung;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + anziehung;
		result = prime * result + geschw;
		result = prime * result + ((posDavor == null) ? 0 : posDavor.hashCode());
		result = prime * result + schadenNormal;
		result = prime * result + schadenVerfolgung;
		result = prime * result + weg;
		result = prime * result + ((ziel == null) ? 0 : ziel.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		VerfolgerGeschoss other = (VerfolgerGeschoss) obj;
		if (anziehung != other.anziehung)
			return false;
		if (geschw != other.geschw)
			return false;
		if (posDavor == null) {
			if (other.posDavor != null)
				return false;
		} else if (!posDavor.equals(other.posDavor))
			return false;
		if (schadenNormal != other.schadenNormal)
			return false;
		if (schadenVerfolgung != other.schadenVerfolgung)
			return false;
		if (weg != other.weg)
			return false;
		if (ziel == null) {
			if (other.ziel != null)
				return false;
		} else if (!ziel.equals(other.ziel))
			return false;
		return true;
	}
}
