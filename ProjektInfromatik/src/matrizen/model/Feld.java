package matrizen.model;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import matrizen.core.DateiManager;
import matrizen.core.Vektor;
import matrizen.view.SpielFenster;

public class Feld extends Grafisch {
	private boolean solide;
	private Typ t;
	private Vektor raster;
	private final float faktorX, faktorY;

	public Feld(Typ t, Vektor raster) {
		// this(t.gibGrafik(), t.solide);
		this.t = t;
		this.raster = raster;
		faktorX = SpielFenster.hoehe/Spiel.zeilen;
		faktorY = SpielFenster.breite/Spiel.spalten;
		grafik = DateiManager.laden(DateiManager.Bild.zufaelligeGrafik(t));
	}

	/*
	 * private Feld(BufferedImage grafik, boolean solide) { super(); this.grafik
	 * = grafik; this.solide = solide; }
	 */

	@Override
	public void zeichnen(Graphics2D g) {
		g.drawImage(grafik, (int) (raster.getX() * faktorX), (int) (raster.getY() * faktorY), (int) SpielFenster.breite / Spiel.zeilen,
				(int) SpielFenster.hoehe / Spiel.spalten, null);
	}
	
	public String toString() {		
		return "Feld:{solid=" + solide + ";Type=" + t + ";pos=" + raster.toString() + "}";
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

	public enum Typ {
		WASSER(true), WIESE(false), BAUM(true), STEIN(false), STEINCHEN(true), ERDE(false), SCHOTTER(false);

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
