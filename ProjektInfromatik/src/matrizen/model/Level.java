package matrizen.model;

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
}
