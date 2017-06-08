package matrizen.view.hud;

import java.awt.Color;
import java.awt.Graphics2D;

import matrizen.core.Vektor;
import matrizen.model.Grafikbasis;

public class LebensAnzeige extends Grafikbasis {
	public static final int breite = 50, hoehe = 10;
	private int maxLeben, leben;
	private Vektor pos;
	
	public LebensAnzeige(int leben, int maxLeben, Vektor pos) {
		this.leben = leben;
		this.maxLeben = maxLeben;
		this.pos = pos;
	}
	
	@Override
	public void zeichnen(Graphics2D g) {
		g.setColor(Color.RED);
		g.drawRect((int) pos.getX(), (int) pos.getY(), breite, hoehe);
		g.setColor(Color.black);
		g.drawRect((int) pos.getX() + 5, (int) pos.getY() + 5, breite * maxLeben/leben, hoehe - 10);
	}

}
