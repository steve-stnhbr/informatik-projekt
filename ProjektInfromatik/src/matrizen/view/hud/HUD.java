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
		private final static int w = 100, h = 25, off = 3, x = 5, y = 5;

		public static void zeichnen(Graphics2D g) {
			g.setColor(Color.white);
			g.fillRect(x, y, w, h);
			g.setColor(Color.lightGray);
			g.fillRect(x + off, y + off, w - off * 2, h - off * 2);
			g.setColor(Color.red);
			g.fillRect(x + off, y + off,
					(w * Spieler.gibInstanz().getLeben() / Spieler.gibInstanz().maxLeben) - off * 2, h - off * 2);
		}
	}

}
