package matrizen.model.elemente;

import java.awt.Graphics2D;

import matrizen.core.DateiManager;
import matrizen.core.Vektor;
import matrizen.model.Gegenstand;

public class Item extends Gegenstand {
	private ItemTyp typ;
	
	public Item(ItemTyp t) {
		this.typ = t;
		this.grafik = DateiManager.laden(DateiManager.Bild.gegenstandLaden(t));
	}
	
	@Override
	public void kraftAusueben(Vektor v) {
		
	}

	@Override
	public void zeichnen(Graphics2D g) {
		
	}

	public enum ItemTyp {
		schluessel,
		upgrade1,
		updrade2;
	}
}
