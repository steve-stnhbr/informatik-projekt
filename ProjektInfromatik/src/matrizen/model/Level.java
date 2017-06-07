package matrizen.model;

import java.awt.Graphics2D;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import matrizen.core.DateiManager;
import matrizen.core.Vektor;
import matrizen.model.elemente.Figur;
import matrizen.model.elemente.Gegner;
import matrizen.model.elemente.Geschoss;
import matrizen.model.elemente.Item;
import matrizen.model.elemente.Spieler;
import matrizen.view.SpielFenster;

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
		this(new CopyOnWriteArrayList<Levelelement>(), felder);
	}

	public Level(List<Levelelement> liste, Feld[][] felder) {
		this.liste = liste;
		this.felder = felder;
	}

	public void zeichnen(Graphics2D g) {
		kollisionUeberpruefen();

		for (Feld[] felds : felder) {
			for (Feld feld : felds) {
				feld.zeichnen(g);
			}
		}

		for (Levelelement l : liste) {
			l.zeichnen(g);
			checkPosition(l);
		}
	}

	private void kollisionUeberpruefen() {
		for (Levelelement l0 : liste) {
			for (Levelelement l1 : liste) {
				if (l0 instanceof Geschoss && l1 instanceof Figur) {
					Geschoss g = (Geschoss) l0;
					if (g.getPos().dist(l1.getPos()) < g.getTyp().getRadius()) {
						if (g.isSpieler())
							Spieler.gibInstanz().schaden(g.getSchaden());
						else
							((Figur) l1).schaden(g.getSchaden());
					}
				}

				if (l1 instanceof Item
						&& Spieler.gibInstanz().getPos().kopieren().div(32f).equals(l1.getPos().kopieren().div(32))) {
					Spieler.gibInstanz().aufsammeln((Item) l1);
					liste.remove(l1);
				}
			}
		}
	}

	private void checkPosition(Levelelement l) {
		if (l.pos.getX() > SpielFenster.breite + 100 || l.pos.getY() > SpielFenster.hoehe + 100 || l.pos.getX() < -100
				|| l.pos.getY() < -100)
			liste.remove(l);

	}

	public void hinzufuegen(Levelelement l) {
		liste.add(l);
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

	public Feld getFeld(int x, int y) {
		return felder[x][y];
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

	public Feld getFeld(Vektor v) {
		return getFeld((int) v.getX(), (int) v.getY());
	}

	public boolean istGegner(Vektor v) {
		for (Levelelement l : liste)
			if (l instanceof Gegner && l.getPos().kopieren().div(32).equals(v))
				return true;
		return false;
	}
}
