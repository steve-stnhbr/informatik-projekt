package matrizen.model.gegner;

import matrizen.core.Vektor;
import matrizen.model.elemente.Gegner;
import matrizen.model.elemente.Geschoss;

public class Zauberer extends Gegner {
	
	public Zauberer() {
		
	}
	
	public void angriff() {
		new Geschoss(Geschoss.Typ.kleinBlau, this.pos, Vektor.nullVektor, false);
	}
}
