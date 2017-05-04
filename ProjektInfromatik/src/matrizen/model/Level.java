package matrizen.model;

import java.util.ArrayList;
import java.util.List;

public class Level {
	private List<Levelelement> liste;
	private Feld[][] felder;
	
	public Level(){
		liste = new ArrayList<Levelelement>();
		felder = new Feld[Spiel.zeilen][Spiel.spalten];
	}
}
