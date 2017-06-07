package matrizen.model.elemente;

import matrizen.model.Levelelement;

public abstract class Figur extends Levelelement {
	protected int leben;
	
	public void schaden(int i) {
		leben -= i;
	}
	
	public int getLeben() {
		return leben;
	}

	public abstract void beimTod();
}
