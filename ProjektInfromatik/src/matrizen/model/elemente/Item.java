package matrizen.model.elemente;

import java.awt.Graphics2D;

import matrizen.core.DateiManager;
import matrizen.core.Vektor;
import matrizen.model.Gegenstand;

public class Item extends Gegenstand {
	private Typ typ;
	
	public Item(Typ t) {
		this.typ = t;
		this.grafik = DateiManager.laden(DateiManager.Bild.zufaelligeGrafik(t));
	}

	@Override
	public void zeichnen(Graphics2D g) {
		g.drawImage(grafik, (int) pos.getX(), (int) pos.getY(), null); 
	}

	public enum Typ implements GrafikTyp {
		schluessel;
	}
}
