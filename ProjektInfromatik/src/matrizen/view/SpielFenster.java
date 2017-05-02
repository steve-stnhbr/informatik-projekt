package matrizen.view;

import javax.swing.JFrame;

public class SpielFenster extends JFrame {
	private static SpielFenster instanz;
	
	private SpielFenster() {
		
	}
	
	public static SpielFenster gibInstanz() {
		if(instanz == null)
			instanz = new SpielFenster();
		return instanz;
	}
}
