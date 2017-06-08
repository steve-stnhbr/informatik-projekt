package matrizen.view.hud;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import matrizen.core.DateiManager;
import matrizen.model.Spiel;
import matrizen.view.SpielFenster;

public class Text {
	private String[] string;

	public Text(String... string) {
		this.string = string;
	}

	/*
	 * private List<String> formatieren(String string) { List<String> strings =
	 * new ArrayList<String>();
	 * 
	 * for(int i = 0; i < string.length() / 32; i++) {
	 * 
	 * }
	 * 
	 * return strings; }
	 */

	public void zeichnen(Graphics2D graphics) {
		// TODO
		int gW = Spiel.spalten * 32, gH = Spiel.zeilen * 32;
		int x = (int) (gW * 0.01), y = (int) (gH - gH * 0.175), w = (int) (gW * .98), h = (int) (gH * .13);
		graphics.setStroke(new BasicStroke(.5f));
		graphics.setColor(Color.black);
		graphics.fillRoundRect(x, y, w, h, 10, 10);
		graphics.setColor(Color.white);
		graphics.drawRoundRect(x, y, w, h, 10, 10);

		try {
			graphics.setFont(
					Font.createFont(Font.TRUETYPE_FONT, new File(DateiManager.pfad + "res/schrift/prstartk.ttf"))
							.deriveFont(9.125f));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}

		graphics.setColor(Color.white);

		for (int i = 0; i < string.length; i++) {
			graphics.drawString(string[i], x + 10, y + (i + 1) * 15);
		}
	}

}
