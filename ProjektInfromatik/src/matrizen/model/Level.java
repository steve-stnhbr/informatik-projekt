package matrizen.model;

import java.awt.Graphics2D;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import matrizen.core.DateiManager;
import matrizen.core.Utils;
import matrizen.core.Vektor;
import matrizen.model.Feld.Typ;
import matrizen.model.elemente.Figur;
import matrizen.model.elemente.Gegner;
import matrizen.model.elemente.Geschoss;
import matrizen.model.elemente.Item;
import matrizen.model.elemente.Spieler;
import matrizen.model.gegner.DracheGegner;
import matrizen.model.gegner.FledermausGegner;
import matrizen.model.gegner.HexeGegner;
import matrizen.model.gegner.RitterGegner;
import matrizen.model.gegner.ZombieGegner;
import matrizen.view.SpielFenster;

public class Level {
	public static Level level0 = DateiManager.laden(DateiManager.Level.level0);
	public static Level level1 = DateiManager.laden(DateiManager.Level.level1);
	public static Level level2 = DateiManager.laden(DateiManager.Level.level2);
	public static Level level3 = DateiManager.laden(DateiManager.Level.level3);

	private final int spawnDelay = DateiManager.werte.get("level_spawn_delay"),
			zombieWahrscheinlichkeit = DateiManager.werte.get("level_wahrsch_zombie"),
			ritterWahrscheinlichkeit = DateiManager.werte.get("level_wahrsch_ritter"),
			hexeWahrscheinlichkeit = DateiManager.werte.get("level_wahrsch_hexe"),
			dracheWahrscheinlichkeit = DateiManager.werte.get("level_wahrsch_drache");

	private List<Levelelement> liste;
	private Feld[][] felder;
	private Level naechstesLevel;
	private Vektor startPosition;

	public Level() {
		this(new Feld[Spiel.zeilen][Spiel.spalten]);
	}

	public Level(Feld[][] felder) {
		this(new CopyOnWriteArrayList<Levelelement>(), felder, null);
	}

	public Level(List<Levelelement> liste, Feld[][] felder, Vektor startPosition) {
		this.liste = liste;
		this.felder = felder;
		this.startPosition = startPosition;
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

		spielerPositionUeberpruefen();
		if (Spiel.gibInstanz().ticks % spawnDelay == 0) {
			int r = Utils.random(100);
			if (equals(Level.getLevel(3))) {
				if (r < zombieWahrscheinlichkeit)
					hinzufuegen(new ZombieGegner(erstellePosition()));
				else if (r < zombieWahrscheinlichkeit + ritterWahrscheinlichkeit)
					hinzufuegen(new RitterGegner(erstellePosition()));
				else if (r < zombieWahrscheinlichkeit + ritterWahrscheinlichkeit + hexeWahrscheinlichkeit)
					hinzufuegen(new HexeGegner(erstellePosition(), true));
				else if (r < zombieWahrscheinlichkeit + ritterWahrscheinlichkeit + hexeWahrscheinlichkeit
						+ dracheWahrscheinlichkeit)
					hinzufuegen(new DracheGegner(erstellePosition()));
			} else {
				if (r < zombieWahrscheinlichkeit)
					hinzufuegen(new ZombieGegner(erstellePosition()));
				else if (r < zombieWahrscheinlichkeit + ritterWahrscheinlichkeit)
					hinzufuegen(new RitterGegner(erstellePosition()));
			}
		}

	}

	private Vektor erstellePosition() {
		Vektor v = null;

		do {
			v = new Vektor(Utils.random(Spiel.spalten), Utils.random(Spiel.zeilen));
		} while (istLegal(v));

		return v;

	}

	private boolean istLegal(Vektor v) {
		try {
			return v.getX() >= 0 && v.getY() >= 0 && v.getX() < Spiel.spalten && v.getY() < Spiel.zeilen
					&& !getFeld((int) v.kopieren().div(32).getX(), (int) v.kopieren().div(32).getY()).isSolide();
		} catch (ArrayIndexOutOfBoundsException e) {
			v.div(32);
			return v.getX() >= 0 && v.getY() >= 0 && v.getX() < Spiel.spalten && v.getY() < Spiel.zeilen
					&& !getFeld((int) v.kopieren().div(32).getX(), (int) v.kopieren().div(32).getY()).isSolide();
		}
	}

	private void positionUeberpruefen(Feld feld) {
		if (Spieler.gibInstanz().getPos().kopieren().div(Spiel.feldLaenge).equals(feld.getRaster()))
			feld.beimBetreten();
	}

	// TODO
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
				Spieler.gibInstanz().aufsammeln(((Item) l1));
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
		if (gibAnzahlGegner() == 1) {
			hinzufuegen(new Item(Item.Typ.schluessel, f.getPos().div(Spiel.feldLaenge).round()));
			setFeld(Spieler.gibInstanz().getxFeld(), Spieler.gibInstanz().getyFeld(), Typ.TORZU);

			if (equals(getLevel(2))/* && f instanceof RitterGegner */) {
				Spiel.gibInstanz().getLevel().hinzufuegen(new Item(Item.Typ.stabVerfolgung,
						f.pos.kopieren().div(Spiel.feldLaenge).add(new Vektor(1, 0))));
			}

		}

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

	private void spielerPositionUeberpruefen() {
		Vektor v = new Vektor(Spieler.gibInstanz().getxFeld(), Spieler.gibInstanz().getyFeld())
				.add(Spieler.gibInstanz().getBlick().getFinalVektor());
		if (positionIstLegal(v)) {
			if (getFeld(v).getTyp() == Typ.TORZU && Spieler.gibInstanz().hatSchluessel()) {
				setFeld((int) v.getX(), (int) v.getY(), Typ.TOROFFEN);
				Spieler.gibInstanz().schluesselEntfernen();
			}
		}

	}

	private boolean positionIstLegal(Vektor v) {
		return v.getX() >= 0 && v.getY() >= 0 && v.getX() < Spiel.spalten && v.getY() < Spiel.zeilen;
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
						|| ((Gegner) l).getZiel().equals(v.kopieren())) && !l.equals(f))
					return true;
				else
					;
			else if (l instanceof Gegner && (l.getPos().kopieren().div(Spiel.feldLaenge).equals(v)
					|| ((Gegner) l).getZiel().equals(v.kopieren().mult(Spiel.feldLaenge))))
				return true;
		}

		return false;
	}

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
			@SuppressWarnings("unused")
			Vektor pos = new Vektor(Integer.MIN_VALUE, Integer.MIN_VALUE);

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

		return g.getPos().equals(new Vektor(Integer.MIN_VALUE, Integer.MIN_VALUE)) ? null : g;
	}

	public void alleEntfernen(List<Geschoss> list) {
		liste.removeAll(list);
	}

	public Vektor getStartPosition() {
		return startPosition == null ? new Vektor(Spieler.gibInstanz().getxFeld(), Spieler.gibInstanz().getyFeld())
				: startPosition;
	}

	public static Level getLevel(int i) {
		switch (i) {
		case 0:
			if (level0 == null)
				level0 = DateiManager.laden(DateiManager.Level.level0);
			return level0;
		case 1:
			if (level1 == null)
				level1 = DateiManager.laden(DateiManager.Level.level1);
			return level1;
		case 2:
			if (level2 == null)
				level2 = DateiManager.laden(DateiManager.Level.level2);
			return level2;
		case 3:
			if (level3 == null)
				level3 = DateiManager.laden(DateiManager.Level.level3);
			return level3;
		default:
			return null;
		}
	}

	static void reset() {
		level0 = null;
		level1 = null;
		level2 = null;
		level3 = null;
	}
}
