package matrizen.model;

import java.awt.Color;

public abstract class Zauberstab {
	protected int schaden, delay, reichweite, geschw;
	
	public abstract void schuss();

	public void aktualisieren() {
	}
	
	public int getDelay() {
		return delay;
	}

	public abstract Color getFarbe(); 
}
