package matrizen.model;

import java.awt.Graphics2D;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import matrizen.core.DateiManager;
import matrizen.core.Vektor;
import matrizen.model.Feld.Typ;
import matrizen.model.elemente.Figur;
import matrizen.model.elemente.Gegner;
import matrizen.model.elemente.Geschoss;
import matrizen.model.elemente.Item;
import matrizen.model.elemente.Spieler;
import matrizen.view.SpielFenster;

public class Level {
	public static final Level level0 = DateiManager.laden(DateiManager.Level.level0);
	public static final Level level1 = DateiManager.laden(DateiManager.Level.level1);
	public static final Level level2 = DateiManager.laden(DateiManager.Level.level2);
	public static final Level level3 = DateiManager.laden(DateiManager.Level.level3);
	
	private List<Levelelement> liste;
	private Feld[][] felder;
	private Level naechstesLevel;

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
		for (Feld[] felds : felder) {
			for (Feld feld : felds) {
				feld.zeichnen(g);
				positionUeberpruefen(feld);
			}
		}

		for (Levelelement l : liste) {
			kollisionUeberpruefen(l);
			l.zeichnen(g);
			checkPosition(l);
		}
	}

	private void positionUeberpruefen(Feld feld) {
		if(Spieler.gibInstanz().getPos().kopieren().div(32).equals(feld.getRaster()))
			feld.beimBetreten();		
	}

	private void kollisionUeberpruefen(Levelelement l0) {
			for (Levelelement l1 : liste) {
				if (l0 instanceof Geschoss && l1 instanceof Figur) {
					Geschoss g = (Geschoss) l0;
					if (g.getPos().dist(l1.getPos().kopieren()) < g.getTyp().getRadius()) {
						if (g.isSpieler())
							((Figur) l1).schaden(g.getSchaden());
						else
							Spieler.gibInstanz().schaden(g.getSchaden());
						liste.remove(g);

						if (((Figur) l1).getLeben() <= 0) {
							liste.remove(l1);
							((Figur) l1).beimTod();
							l1 = null;
						}
					}
				}

				if (l1 instanceof Item
						&& Spieler.gibInstanz().getPos().kopieren().div(32f).equals(l1.getPos().kopieren().div(32))) {
					((Item) l1).beimAufheben();
					liste.remove(l1);
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
		return liste.equals(other.liste) && felder.equals(other.felder) && naechstesLevel.equals(other.naechstesLevel);
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

	public Feld getFeld(Vektor v) {
		return getFeld((int) v.getX(), (int) v.getY());
	}

	public boolean istGegner(Vektor v) {
		for (Levelelement l : liste)
			if (l instanceof Gegner && l.getPos().kopieren().div(32).equals(v))
				return true;
		return false;
	}

	public void setFeld(int x, int y, Typ t) {
		felder[x][y] = new Feld(t, new Vektor(x, y));
	}

	public Level getNaechstesLevel() {
		return naechstesLevel;
	}

	public void setNaechstesLevel(Level naechstesLevel) {
		this.naechstesLevel = naechstesLevel;		
	}
}
