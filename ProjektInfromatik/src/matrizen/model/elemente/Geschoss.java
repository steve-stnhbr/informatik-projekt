package matrizen.model.elemente;

import java.awt.Graphics2D;

import matrizen.core.Vektor;
import matrizen.model.Gegenstand;

public class Geschoss extends Gegenstand {

	
	public Geschoss(Typ t) {
		
	}

	@Override
	public void zeichnen(Graphics2D g) {
		g.drawImage(grafik, (int) pos.getX(), (int) pos.getY(), null);
	}

	public enum Typ implements GrafikTyp {
		
	}
}
