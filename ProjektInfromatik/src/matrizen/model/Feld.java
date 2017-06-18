package matrizen.model;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import matrizen.core.DateiManager;
import matrizen.core.Vektor;
import matrizen.model.elemente.GrafikTyp;

public class Feld extends Grafikbasis {
	private boolean solide;
	private Typ t;
	private Vektor raster;

	public Feld(Typ t, Vektor raster) {
		this.t = t;
		this.raster = raster;
		grafik = DateiManager.laden(DateiManager.Bild.zufaelligeGrafik(t));
		this.solide = t.solide;
	}

	@Override
	public void zeichnen(Graphics2D g) {
		g.drawImage(grafik, (int) (raster.getX() * Spiel.feldLaenge), (int) (raster.getY() * Spiel.feldLaenge),
				(int) Spiel.feldLaenge, (int) Spiel.feldLaenge, null);
	}

	public String toString() {
		return "Feld:{solid=" + solide + ";Type=" + t + ";pos=" + raster.toString() + "}";
	}

	public boolean equals(Feld other) {
		return other.solide == solide && other.t == t && other.raster.equals(raster);
	}

	public boolean isSolide() {
		return solide;
	}

	public void setSolide(boolean solide) {
		this.solide = solide;
	}

	public Typ getTyp() {
		return t;
	}

	public void setTyp(Typ t) {
		this.t = t;
	}

	public Vektor getRaster() {
		return raster;
	}

	public void setRaster(Vektor raster) {
		this.raster = raster;
	}

	public void beimBetreten() {
		t.beimBetreten();
	}

	public enum Typ implements GrafikTyp {
		WIESE(false),
		ERDE(false),
		WASSER(true),
		BAUM(true),
		WEITER(false) {
			public void beimBetreten() {
				if ((Spiel.gibInstanz().kannTeleportieren && Spiel.gibInstanz().tutorial)
						|| !Spiel.gibInstanz().tutorial)
					Spiel.gibInstanz().setLevel(Spiel.gibInstanz().getLevel().getNaechstesLevel());
			}
		},
		STEIN(false),
		STEINCHEN(true),
		SCHOTTER(false),
		BRUECKE(false),
		HECKE(true);

		final boolean solide;

		private Typ(boolean solide) {
			this.solide = solide;
		}

		public void beimBetreten() {
			return;
		}

		public BufferedImage gibGrafik() {
			return DateiManager.laden(DateiManager.Bild.zufaelligeGrafik(this));
		}

		public static Typ gibTyp(int i) {
			return values()[i];
		}

		public static int gibIndex(Typ t) {
			for (int i = 0; i < values().length; i++)
				if (values()[i] == t)
					return i;
			return -1;
		}
	}
}