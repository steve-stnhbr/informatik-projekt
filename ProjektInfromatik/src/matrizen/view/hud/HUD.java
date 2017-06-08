package matrizen.view.hud;

import java.awt.Graphics2D;

import matrizen.model.Grafikbasis;

public class HUD extends Grafikbasis {
	private static HUD instanz;
	
	private HUD() {
		
	}
	
	public static HUD gibInstanz() {
		if(instanz == null)
			instanz = new HUD();
		return instanz;
	}

	@Override
	public void zeichnen(Graphics2D g) {
		
	}
	
	
	
}
