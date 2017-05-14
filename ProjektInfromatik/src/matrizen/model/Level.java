package matrizen.model;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import matrizen.core.DateiManager;

public class Level {
	// nur zum testen
	public static final Level anfangsLevel = DateiManager.laden(DateiManager.Level.level1);
	// TODO
	private List<Levelelement> liste;
	private Feld[][] felder;
	private Level levelOben, levelUnten, levelLinks, levelRechts;

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

	public void zeichnen(Graphics2D g) {
		for (Feld[] felds : felder) {
			for (Feld feld : felds) {
				feld.zeichnen(g);
			}
		}

		for (Levelelement l : liste) {
			l.zeichnen(g);
		}
	}

	public String toString() {
		String s = "Level:{felder=[";

		for (Feld[] felds : felder) {
			for (Feld feld : felds) {
				s += feld.toString();
			}
		}

		s += "];elemente=[";

		for (Levelelement l : liste) {
			s += l.toString();
		}

		return s + "]}";
	}

	public boolean equals(Level other) {
		return liste.equals(other.liste) && felder.equals(other.felder) && levelOben.equals(other.levelOben)
				&& levelRechts.equals(other.levelRechts) && levelUnten.equals(other.levelUnten)
				&& levelLinks.equals(other.levelLinks);
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

	public Level getLevelOben() {
		return levelOben;
	}

	public void setLevelOben(Level levelOben) {
		this.levelOben = levelOben;
	}

	public Level getLevelUnten() {
		return levelUnten;
	}

	public void setLevelUnten(Level levelUnten) {
		this.levelUnten = levelUnten;
	}

	public Level getLevelLinks() {
		return levelLinks;
	}

	public void setLevelLinks(Level levelLinks) {
		this.levelLinks = levelLinks;
	}

	public Level getLevelRechts() {
		return levelRechts;
	}

	public void setLevelRechts(Level levelRechts) {
		this.levelRechts = levelRechts;
	}
}
