package matrizen.vorhinein;

import javax.swing.JPanel;

public class EingabePanel extends JPanel {
	private static final long serialVersionUID = 6479304563663625457L;
	private static EingabePanel instanz;

	//TODO
	
	public static EingabePanel gibInstanz() {
		if (instanz == null)
			instanz = new EingabePanel();
		return instanz;
	}

}
