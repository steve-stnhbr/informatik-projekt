package matrizen.view.hud;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import matrizen.core.DateiManager;
import matrizen.core.DateiManager.Bild;
import matrizen.model.Grafikbasis;
import matrizen.model.Spiel;
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
		Muenzen.zeichnen(g);
	}

	static class Muenzen {
		private static final int x = Spiel.spalten * Spiel.feldLaenge - 52, y = 1;

		public static void zeichnen(Graphics2D g) {
			g.drawImage(DateiManager.laden(Bild.itemMuenze), x, y, 25, 25, null);
			try {
				g.setFont(Font.createFont(Font.TRUETYPE_FONT, new File(DateiManager.pfad + "res/schrift/prstartk.ttf"))
						.deriveFont(25f));
			} catch (FontFormatException | IOException e) {
				e.printStackTrace();
			}
			g.setColor(Color.white);
			g.drawString("=" + Spieler.gibInstanz().gibAnzahlMuenzen(), x + 18, y + 17);
		}

	}

	static class SpielerLeben {
		private final static int w = 100, h = 10, off = 2, x = 5, y = 5;

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
