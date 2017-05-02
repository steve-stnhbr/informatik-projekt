package matrizen.gui;

import javax.swing.JFrame;

public class SpielFenster extends JFrame {
	private static SpielFenster instanz;
	
	
	public static SpielFenster gibInstanz() {
		if(instanz == null)
			instanz = new SpielFenster();
		return instanz;
	}
}
