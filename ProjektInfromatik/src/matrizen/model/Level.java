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
import matrizen.model.gegner.FledermausGegner;
import matrizen.model.gegner.HexeGegner;
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
		if (Spieler.gibInstanz().getPos().kopieren().div(Spiel.feldLaenge).equals(feld.getRaster()))
			feld.beimBetreten();
	}

	private void kollisionUeberpruefen(Levelelement l0) {
		for (Levelelement l1 : liste) {
			if (l0 instanceof Geschoss && l1 instanceof Figur) {
				Geschoss g = (Geschoss) l0;
				Figur f = (Figur) l1;

				if (g.isAktiv()) {

					if (g.isSpieler()) {
						if (g.getPos().dist(f.getPos()) <= g.getTyp().getRadius()) {
							if (l1 instanceof HexeGegner && Spiel.gibInstanz().tutorials[1]
									&& !((HexeGegner) l1).isAktiv()) {
								if (!Spiel.gibInstanz().tutorials[2])
									Spiel.gibInstanz().tutorialTick = (int) Spiel.gibInstanz().ticks;
								Spiel.gibInstanz().tutorials[2] = true;
							}

							f.schaden(g.getSchaden());
							liste.remove(g);
						}
					} else {
						if (g.getPos().dist(Spieler.gibInstanz().getPos()) <= g.getTyp().getRadius()) {
							Spieler.gibInstanz().schaden(g.getSchaden());
							liste.remove(g);
						}
					}
				} else {
					liste.remove(g);
					g = null;
				}

				if (((Figur) l1).getLeben() <= 0) {
					if (Spiel.gibInstanz().tutorial && Spiel.gibInstanz().tutorials[2]
							&& Spiel.gibInstanz().gegnerKannSterben) {
						if (!Spiel.gibInstanz().tutorials[3])
							Spiel.gibInstanz().tutorialTick = (int) Spiel.gibInstanz().ticks;
						Spiel.gibInstanz().tutorials[3] = true;
					}
					if (Spiel.gibInstanz().tutorial && Spiel.gibInstanz().gegnerKannSterben
							|| !Spiel.gibInstanz().tutorial) {
						figurEntfernen(((Figur) l1));
					}
				}
			}

			if (l1 instanceof Item && (Spiel.gibInstanz().schluesselAufheben || !Spiel.gibInstanz().tutorial)
					&& Spieler.gibInstanz().getPos().kopieren().div(Spiel.feldLaenge)
							.equals(l1.getPos().kopieren().div(Spiel.feldLaenge))) {
				((Item) l1).beimAufheben();
				if (((Item) l1).getTyp() == Item.Typ.schluessel) {
					if (!Spiel.gibInstanz().tutorials[4])
						Spiel.gibInstanz().tutorialTick = (int) Spiel.gibInstanz().ticks;
					Spiel.gibInstanz().tutorials[4] = true;
				} else if (((Item) l1).getTyp() == Item.Typ.herz) {
					if (!Spiel.gibInstanz().tutorials[6])
						Spiel.gibInstanz().tutorialTick = (int) Spiel.gibInstanz().ticks;
					Spiel.gibInstanz().tutorials[6] = true;
				}
				liste.remove(l1);
			}
		}

	}

	private void figurEntfernen(Figur f) {
		if (gibAnzahlGegner() == 1)
			hinzufuegen(new Item(Item.Typ.schluessel, f.getPos().div(Spiel.feldLaenge)));

		liste.remove(f);
		f.beimTod();
		f = null;

	}

	private int gibAnzahlGegner() {
		int a = 0;

		for (Levelelement l : liste)
			if (l instanceof Gegner && !(l instanceof FledermausGegner))
				a++;

		return a;
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
		return liste.equals(other.liste) && felder.equals(other.felder);
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

	public boolean istGegner(Vektor v, Figur f) {
		for (Levelelement l : liste) {
			if (f != null)
				if (l instanceof Gegner && (l.getPos().kopieren().div(Spiel.feldLaenge).equals(v)
						|| ((Gegner) l).getZiel().equals(v.kopieren().mult(Spiel.feldLaenge))) && !l.equals(f))
					return true;
				else
					;
			else if (l instanceof Gegner && (l.getPos().kopieren().div(Spiel.feldLaenge).equals(v)
					|| ((Gegner) l).getZiel().equals(v.kopieren().mult(Spiel.feldLaenge))))
				return true;
		}

		return false;
	}

	// TODO
	public boolean istGegnerSpieler(Vektor v, Figur f) {
		for (Levelelement l : liste) {
			if (l instanceof Gegner)
				if (f != null) {
					if ((l.getPos().kopieren().div(Spiel.feldLaenge).equals(v)
							|| ((Gegner) l).getZiel().equals(v.kopieren().mult(Spiel.feldLaenge))) && !l.equals(f)
							&& !(l instanceof FledermausGegner))
						return true;
				} else if ((l.getPos().kopieren().div(Spiel.feldLaenge).equals(v)
						|| ((Gegner) l).getZiel().equals(v.kopieren().mult(Spiel.feldLaenge)))
						&& !(l instanceof FledermausGegner))
					return true;
		}

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

	public void entfernen(Levelelement l) {
		liste.remove(l);
	}

	public Figur gibNaechstenGegner(Vektor pos) {
		Gegner g = new Gegner() {
			Vektor pos = new Vektor(-1000, -1000);

			public void zeichnen(Graphics2D g) {
			}

			public void beimTod() {
			}

			public Vektor getZiel() {
				return null;
			}

			public boolean angriff() {
				return false;
			}
		};

		for (Levelelement l : liste)
			if (l instanceof Gegner && l.getPos().dist(pos) < pos.dist(g.getPos()))
				g = (Gegner) l;

		return g.getPos().equals(new Vektor(-1000, -1000)) ? null : g;
	}
}
