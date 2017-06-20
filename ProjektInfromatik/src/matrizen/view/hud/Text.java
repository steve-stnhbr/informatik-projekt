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
	private static final int schritt = 5;
	int gW = Spiel.spalten * 32, gH = Spiel.zeilen * 32;
	final int fX = (int) (gW * 0.01), fY = (int) (gH - gH * 0.175), w = (int) (gW * .98), h = (int) (gH * .13);
	int x = fX, y;
	private String[] string;
	private int richtung;

	public Text(int richtung, String... string) {
		this.richtung = richtung;
		this.string = string;
		
		y = richtung == 1 ? fY + h + 20 : fY;
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
		
		
		graphics.setStroke(new BasicStroke(.5f));
		graphics.setColor(Color.black);
		graphics.fillRoundRect(x, y, w, h, 10, 10);
		graphics.setColor(Color.white);
		graphics.drawRoundRect(x, y, w, h, 10, 10);
		
		try {
			graphics.setFont(
					Font.createFont(Font.TRUETYPE_FONT, new File(DateiManager.pfad + "res/schrift/prstartk.ttf"))
							.deriveFont(22f));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}

		graphics.setColor(Color.white);

		for (int i = 0; i < string.length; i++) {
			graphics.drawString(string[i], x + 10, y + (i + 1) * 15);
		}
		
		if(y > fY && richtung == 1)
			y -= schritt;
		else if(richtung == -1)
			y += schritt;
		else
			y = fY;
	}

}
