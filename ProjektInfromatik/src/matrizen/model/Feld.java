package matrizen.model;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import matrizen.core.DateiManager;

public class Feld extends Grafisch {
	private boolean solide;

	public Feld(Typ t) {
		this(t.gibGrafik(), t.solide);
	}
	
	private Feld(BufferedImage grafik, boolean solide) {
		super();
		this.grafik = grafik;
		this.solide = solide;
	}
	
	@Override
	public void zeichnen(Graphics2D g) {
		
	}
	
	public enum Typ {
		WASSER(true),
		WIESE(false),
		BAUM(true),
		STEIN(false),
		STEINCHEN(true),
		ERDE(false),
		SCHOTTER(false);
		
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
	}
}
