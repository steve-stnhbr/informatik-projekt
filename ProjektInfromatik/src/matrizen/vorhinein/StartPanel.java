package matrizen.vorhinein;

import java.awt.Dimension;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import matrizen.view.SpielFenster;

public class StartPanel extends JPanel {
	private static final long serialVersionUID = -2583996177956094992L;
	private static StartPanel instanz;

	private boolean groesse;
	private JButton bStart, bKonfig, bEnde;

	private StartPanel() {
		initComponents();
		initListeners();
		addComponents();
	}

	public void doLayout() {
		super.doLayout();
	}

	private void initComponents() {
		bStart = new JButton("Spiel starten!");
		bKonfig = new JButton("Einstellungen");
		bEnde = new JButton("Beenden");
	}

	private void initListeners() {
		bStart.addActionListener((e) -> {
			((JFrame) SwingUtilities.getWindowAncestor(this)).setVisible(false);
			SpielFenster.gibInstanz().start();
		});

		bKonfig.addActionListener((e) -> {
			((AnfangsFenster) SwingUtilities.getWindowAncestor(this)).inhaltAendern(EinstellungsPanel.getInstanz());
		});

		bEnde.addActionListener((e) -> System.exit(0));
	}

	private void addComponents() {
		add(bStart);
		add(bKonfig);
		add(bEnde);
	}

	public static StartPanel gibInstanz() {
		if (instanz == null)
			instanz = new StartPanel();
		return instanz;
	}

}
