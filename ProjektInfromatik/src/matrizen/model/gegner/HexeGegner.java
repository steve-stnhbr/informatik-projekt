package matrizen.model.gegner;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import matrizen.core.DateiManager;
import matrizen.core.DateiManager.Bild;
import matrizen.core.Richtung;
import matrizen.core.Utils;
import matrizen.core.Vektor;
import matrizen.model.Level;
import matrizen.model.Spiel;
import matrizen.model.elemente.Gegner;
import matrizen.model.elemente.Geschoss;
import matrizen.model.elemente.Geschoss.Typ;
import matrizen.model.elemente.Item;
import matrizen.model.elemente.Spieler;

public class HexeGegner extends Gegner {
	private final int bewegungDelay = DateiManager.werte.get("hexe_delay_bewegung"),
			angriffDelay = DateiManager.werte.get("hexe_delay_angriff"),
			spezialDelay = DateiManager.werte.get("hexe_delay_spezial"),
			maxLeben = DateiManager.werte.get("hexe_leben"), maxSpawn = DateiManager.werte.get("hexe_spawn_max"),
			minSpawn = DateiManager.werte.get("hexe_spawn_min"), schaden = DateiManager.werte.get("hexe_schaden"),
			weite = DateiManager.werte.get("hexe_weite"),
			bewegungGeschw = DateiManager.werte.get("hexe_bewegung_geschw"),
			dropHerz = DateiManager.werte.get("hexe_drop_herz"),
			dropMuenze = DateiManager.werte.get("hexe_drop_muenze");

	private final float partikelGeschw = DateiManager.werte.get("hexe_partikel_geschw") / 10;

	private boolean aktiv;
	private Vektor ziel;
	private Richtung blick;

	public HexeGegner(Vektor feldPos, boolean aktiv) {
		grafik = DateiManager.laden(Bild.figurHexe);
		pos = feldPos.mult(Spiel.feldLaenge);
		leben = aktiv ? maxLeben : 100;
		ziel = pos;
		blick = Richtung.OBEN;
		this.aktiv = aktiv;

	}

	@Override
	public void zeichnen(Graphics2D g) {
		super.aktualisieren();

		g.drawImage(bildDrehen(grafik), (int) pos.getX(), (int) pos.getY(), (int) Spiel.feldLaenge,
				(int) Spiel.feldLaenge, null);

		if (Spiel.gibInstanz().ticks % bewegungDelay == 0)
			bewegen();
		else if (Spiel.gibInstanz().ticks % angriffDelay == 0 && aktiv)
			angriff();
		else if (Spiel.gibInstanz().ticks % spezialDelay == 0 && aktiv)
			spezialAttacke();

		if (pos.dist(ziel) < ges.mag()) {
			ges = Vektor.nullVektor;
			pos = ziel;
		}

	}

	private void spezialAttacke() {
		int r = Utils.random(minSpawn, maxSpawn);
		while (r > 0) {
			spawnFledermaus();
			r--;
		}
	}

	private void spawnFledermaus() {
		Vektor v = new Vektor(Utils.random(3) - 2, Utils.random(3) - 2);
		Spiel.gibInstanz().getLevel().hinzufuegen(new FledermausGegner(v.add(pos.kopieren().div(Spiel.feldLaenge))));
	}

	private void bewegen() {
		if (pos.equals(ziel) && Spieler.gibInstanz().getPos().dist(pos) > Spiel.feldLaenge) {
			Vektor v = Spieler.gibInstanz().getPos().kopieren().sub(pos), z = Vektor.nullVektor;

			if (Math.abs(v.getX()) > Math.abs(v.getY()))
				z = new Vektor(Math.signum(v.getX()), 0).mult(Spiel.feldLaenge).add(pos);
			else
				z = new Vektor(0, Math.signum(v.getY())).mult(Spiel.feldLaenge).add(pos);

			blick = Richtung.getRichtung(pos, z);

			if (bewegungMoeglich(z))
				ziel = z;
			if (!pos.equals(ziel))
				bes = ziel.kopieren().sub(pos).normalize().mult(bewegungGeschw / 10);
		}
	}

	private Image bildDrehen(BufferedImage b) {
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(blick.getWinkel()), 16, 16);

		AffineTransformOp operation = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return operation.filter(grafik, null);
	}

	public boolean isAktiv() {
		return aktiv;
	}

	@Override
	public boolean angriff() {
		Spiel.gibInstanz().getLevel()
				.hinzufuegen(new Geschoss(Typ.raute, schaden, weite, pos, new Vektor(1, 0).mult(partikelGeschw), this));
		Spiel.gibInstanz().getLevel().hinzufuegen(
				new Geschoss(Typ.raute, schaden, weite, pos, new Vektor(-1, 0).mult(partikelGeschw), this));
		Spiel.gibInstanz().getLevel().hinzufuegen(
				new Geschoss(Typ.raute, schaden, weite, pos, new Vektor(0, -1).mult(partikelGeschw), this));
		Spiel.gibInstanz().getLevel()
				.hinzufuegen(new Geschoss(Typ.raute, schaden, weite, pos, new Vektor(0, 1).mult(partikelGeschw), this));
		return true;
	}

	@Override
	public void beimTod() {
		System.out.println(pos);
		if (aktiv && Spiel.gibInstanz().getLevel().equals(Level.getLevel(3)))
			Spiel.gibInstanz().getLevel().hinzufuegen(
					new Item(Item.Typ.stabVerfolgung, this.pos.kopieren().add(new Vektor(1, 0))));

		if (Spieler.gibInstanz().gibAnzahlMuenzen() != Spieler.gibInstanz().zielMuenzen - 1
				&& !Spiel.gibInstanz().getLevel().equals(Level.getLevel(3))) {
			int r = Utils.random(100);
			Vektor v = pos.kopieren().round();
			if (pos.mag() > 32)
				v = pos.kopieren().div(32).round();

			do {
				int r0 = Utils.random(-1, 1), r1 = Utils.random(-1, 1);

				if (Spiel.gibInstanz().getLevel().isInBounds(v.kopieren().add(new Vektor(r0, r1))))
					v.add(new Vektor(r0, r1));

			} while (Spiel.gibInstanz().getLevel().getFeld(v).isSolide());

			if (r < dropMuenze)
				Spiel.gibInstanz().getLevel().hinzufuegen(new Item(Item.Typ.muenze, v));
			else if (r < dropMuenze + dropHerz)
				Spiel.gibInstanz().getLevel().hinzufuegen(new Item(Item.Typ.herz, v));
		}
	}

	private boolean bewegungMoeglich(Vektor v) {
		// return !Spiel.gibInstanz().getLevel().istGegner(v.div(32))
		// && !Spiel.gibInstanz().getLevel().getFeld(v.div(32)).isSolide();
		return !Spieler.gibInstanz().getZiel().equals(v);
	}

	public Vektor getZiel() {
		return ziel;
	}

}
