package matrizen.view.hud;

import java.awt.Color;
import java.awt.Graphics2D;

import matrizen.model.Grafikbasis;
import matrizen.model.elemente.Spieler;
import matrizen.view.SpielFenster;

public class HUD extends Grafikbasis {
	private static HUD instanz;

	private HUD() {

	}

	public static HUD gibInstanz() {
		if (instanz == null)
			instanz = new HUD();
		return instanz;
	}

	@Override
	public void zeichnen(Graphics2D g) {
		SpielerLeben.zeichnen(g);
	}

	static class SpielerLeben {
		private final static int w = 300, h = 70, off = 3, x = SpielFenster.breite / 32, y = SpielFenster.hoehe / 32;

		public static void zeichnen(Graphics2D g) {
			g.setColor(Color.white);
			g.fillRect(x, y, w, h);
			g.setColor(Color.red);
			g.fillRect(x + off, y + off,
					(w * Spieler.gibInstanz().maxLeben / Spieler.gibInstanz().getLeben()) - off * 2, h - off * 2);
		}
	}

}
