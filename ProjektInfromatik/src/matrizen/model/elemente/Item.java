package matrizen.model.elemente;

import java.awt.Graphics2D;

import matrizen.core.DateiManager;
import matrizen.core.Utils;
import matrizen.core.Vektor;
import matrizen.model.Feld;
import matrizen.model.Gegenstand;
import matrizen.model.Spiel;

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
	
	public void beimAufheben() {
		typ.beimAufheben();
	}

	public Typ getTyp() {
		return typ;
	}

	public enum Typ implements GrafikTyp {
		herz {
			
			@Override
			public void beimAufheben() {
				Spieler.gibInstanz().leben += 20;
			}
		},
		muenze,
		schluessel {
			
			@Override
			public void beimAufheben() {					
				Spiel.gibInstanz().getLevel().setFeld((int) Utils.random(1, Spiel.spalten - 1),
						(int) Utils.random(1, Spiel.zeilen - (Spiel.gibInstanz().tutorial ? 1 : 2)), Feld.Typ.WEITER);
			}
		};
		
		public void beimAufheben() {
			return;
		}
	}
}
