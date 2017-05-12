package matrizen.model;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class Level {
	private List<Levelelement> liste;
	private Feld[][] felder;
	
	public Level() {
		this(new Feld[Spiel.zeilen][Spiel.spalten]);
	}
	
	public Level(Feld[][] felder) {
		this(new ArrayList<Levelelement>(), felder);
	}
	
	public Level(List<Levelelement> liste, Feld[][] felder) {
		this.liste = liste;
		this.felder = felder;
	}

	public List<Levelelement> getListe() {
		return liste;
	}

	public void setListe(List<Levelelement> liste) {
		this.liste = liste;
	}

	public Feld[][] getFelder() {
		return felder;
	}

	public void setFelder(Feld[][] felder) {
		this.felder = felder;
	}

	public void zeichnen(Graphics2D g) {
		for (Feld[] felds : felder) {
			for(Feld feld : felds) {
				feld.zeichnen(g);
			}
		}
		
		for(Levelelement l : liste) {
			l.zeichnen(g);
		}
	}
}
