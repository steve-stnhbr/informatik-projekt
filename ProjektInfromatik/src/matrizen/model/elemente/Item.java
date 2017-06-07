package matrizen.model.elemente;

import java.awt.Graphics2D;

import matrizen.core.DateiManager;
import matrizen.core.Vektor;
import matrizen.model.Gegenstand;

public class Item extends Gegenstand {
	private Typ typ;
	private Vektor posIF;
	
	public Item(Typ t, Vektor posIF) {
		this.typ = t;
		this.posIF = posIF;
		this.grafik = DateiManager.laden(DateiManager.Bild.zufaelligeGrafik(t));
		pos = posIF.kopieren().mult(32);
	}

	@Override
	public void zeichnen(Graphics2D g) {
		g.drawImage(grafik, (int) pos.getX(), (int) pos.getY(), 32, 32, null); 
	}

	public enum Typ implements GrafikTyp {
		schluessel;
	}
}
