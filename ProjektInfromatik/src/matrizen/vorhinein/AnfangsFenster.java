package matrizen.vorhinein;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;

import javax.swing.JFrame;
import javax.swing.JPanel;

import matrizen.view.SpielFenster;

public class AnfangsFenster extends JFrame {
	private static final long serialVersionUID = 3907609549112425887L;
	public static final String textWeiter = "bestätigen", textAbbruch = "abbrechen";
	private static AnfangsFenster instanz;
	private JPanel inhalt;

	private AnfangsFenster(JPanel inhalt) {
		this.inhalt = inhalt;
		setContentPane(inhalt);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	public JPanel getInhalt() {
		return inhalt;
	}

	public void setInhalt(JPanel inhalt) {
		this.inhalt = inhalt;
		setContentPane(inhalt);
	}

	public static AnfangsFenster gibInstanz() {
		return instanz;
	}

	public static void init() {
		instanz = new AnfangsFenster(StartPanel.gibInstanz());
	}

	public static void main(String[] args) {
		// ändert die formatierung der Konsole, des logs
		// ---------------------------------------------------- zeit, ms, level,
		// nachricht, neue zeile
		System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tT:%tL-%4$s: %5$s%6$s%n");
		SpielFenster.logger.setLevel(Level.FINE);
		Handler handler = new ConsoleHandler();
		handler.setLevel(Level.FINE);
		SpielFenster.logger.addHandler(handler);
		SpielFenster.init();
		init();
	}

	public void inhaltAendern(JPanel p) {
		setInhalt(p);
		repaint();
		pack();
		setVisible(true);
	}

}
