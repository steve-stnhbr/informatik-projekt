package matrizen.model;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import matrizen.core.DateiManager;
import matrizen.core.Vektor;
import matrizen.model.elemente.GrafikTyp;
import matrizen.view.SpielFenster;

public class Feld extends Grafisch {
	private static final float faktorX = SpielFenster.hoehe / Spiel.zeilen,
			faktorY = SpielFenster.breite / Spiel.spalten;
	private boolean solide;
	private Typ t;
	private Vektor raster;

	public Feld(Typ t, Vektor raster) {
		// this(t.gibGrafik(), t.solide);
		this.t = t;
		this.raster = raster;
		grafik = DateiManager.laden(DateiManager.Bild.zufaelligeGrafik(t));
		this.solide = t.solide;
	}

	@Override
	public void zeichnen(Graphics2D g) {
		g.drawImage(grafik, (int) raster.getX() * 32, (int) raster.getY() * 32, 32, 32, null);
		/*
		g.drawImage(grafik, (int) (raster.getX() * Spiel.feldLaenge), (int) (raster.getY() * Spiel.feldLaenge),
				(int) SpielFenster.breite / Spiel.spalten, (int) SpielFenster.hoehe / Spiel.zeilen, null);
				*/
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

	public enum Typ implements GrafikTyp {
		WIESE(false),
		ERDE(false),
		WASSER(true),
		BAUM(true),
		STEIN(false),
		STEINCHEN(true),
		SCHOTTER(false),
		BRUECKE(false),
		HECKE(true);

		final boolean solide;

		private Typ(boolean solide) {
			this.solide = solide;
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
