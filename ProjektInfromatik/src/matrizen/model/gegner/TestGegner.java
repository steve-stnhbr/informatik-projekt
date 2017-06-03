package matrizen.model.gegner;

import java.awt.Graphics2D;

import matrizen.core.DateiManager;
import matrizen.core.Vektor;
import matrizen.model.elemente.Gegner;
import matrizen.model.elemente.Geschoss;

public class TestGegner extends Gegner {
	
	public TestGegner(Vektor pos) {
		this.pos = pos.kopieren().mult(32);
		grafik = DateiManager.laden(DateiManager.Bild.elementGegener);
	}
	
	public void angriff() {
		new Geschoss(Geschoss.Typ.kleinBlau, this.pos, Vektor.nullVektor, false);
	}

	@Override
	public void zeichnen(Graphics2D g) {
		super.aktualisieren();
		g.drawImage(grafik, (int) pos.getX(), (int) pos.getY(), 32, 32, null);
	}
}
