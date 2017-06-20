package matrizen.model.elemente;

import java.awt.Graphics2D;

import matrizen.core.DateiManager;
import matrizen.core.Utils;
import matrizen.core.Vektor;
import matrizen.model.Feld;
import matrizen.model.Gegenstand;
import matrizen.model.Spiel;

public class Item extends Gegenstand {
	private final static int herzRegeneration = DateiManager.werte.get("herz_regeneration");

	private Typ typ;
	private Vektor posIF;

	public Item(Typ t, Vektor posIF) {
		this.typ = t;
		this.posIF = posIF;
		this.grafik = DateiManager.laden(DateiManager.Bild.zufaelligeGrafik(t));
		pos = posIF.kopieren().mult(Spiel.feldLaenge);
	}

	@Override
	public void zeichnen(Graphics2D g) {
		g.drawImage(grafik, (int) pos.getX(), (int) pos.getY(), (int) Spiel.feldLaenge, (int) Spiel.feldLaenge, null);
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
				Spieler.gibInstanz().leben += herzRegeneration;
			}
		},
		muenze,
		schluessel {

			@Override
			public void beimAufheben() {
			}
		};

		public void beimAufheben() {
			return;
		}
	}
}
