package matrizen.model.gegner;

import static matrizen.view.SpielFenster.logger;

import java.awt.Graphics2D;

import matrizen.core.DateiManager;
import matrizen.core.Vektor;
import matrizen.model.Spiel;
import matrizen.model.elemente.Gegner;
import matrizen.model.elemente.Geschoss;
import matrizen.model.elemente.Item;
import matrizen.model.elemente.Item.Typ;

public class TestGegner extends Gegner {
	public static final int maxLeben = 20;
	private boolean tot;

	public TestGegner(Vektor pos) {
		super();
		super.schaden(-maxLeben);
		this.pos = pos.kopieren().mult(32);
		grafik = DateiManager.laden(DateiManager.Bild.figurGegener);
	}

	public void angriff() {
		new Geschoss(Geschoss.Typ.kleinBlau, 10, this.pos, Vektor.nullVektor, false);
	}

	@Override
	public void zeichnen(Graphics2D g) {
		super.aktualisieren();
		g.drawImage(grafik, (int) pos.getX(), (int) pos.getY(), 32, 32, null);
	}

	@Override
	public void beimTod() {
		if (!tot)
			Spiel.gibInstanz().getLevel().hinzufuegen(new Item(Typ.schluessel, this.pos.kopieren().div(32)));
		tot = true;
	}
}
