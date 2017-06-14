package matrizen.vorhinein;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import matrizen.core.DateiManager;
import matrizen.model.Spiel;
import matrizen.view.SpielFenster;

public class StartPanel extends JPanel implements Benennbar {
	private static final long serialVersionUID = -2583996177956094992L;
	private static StartPanel instanz;

	private JButton bStart, bKonfig, bEnde;
	private JCheckBox cTutorial;

	private StartPanel() {
		initComponents();
		initListeners();
		addComponents();

		setPreferredSize(new Dimension(166, 182));

		setVisible(true);
	}

	private void initComponents() {
		bStart = new JButton("Spiel starten!");
		cTutorial = new JCheckBox("Tutorial spielen");
		bKonfig = new JButton("Einstellungen");
		bEnde = new JButton("Beenden");

		cTutorial.setSelected(DateiManager.config.getTutorial() == 0);
	}

	private void initListeners() {
		bStart.addActionListener((e) -> {
			((JFrame) SwingUtilities.getWindowAncestor(this)).setVisible(false);
			Spiel.gibInstanz().tutorial = cTutorial.isSelected();
			SpielFenster.gibInstanz().start();
		});

		bKonfig.addActionListener((e) -> {
			AnfangsFenster.gibInstanz().inhaltAendern(EinstellungsPanel.gibInstanz());
		});

		bEnde.addActionListener((e) -> {
			DateiManager.configSchreiben();
			System.exit(0);
		});

		cTutorial.addActionListener((e) -> {
			DateiManager.config.setTutorial((short) (cTutorial.isSelected() ? 0 : 2));
			System.out.println(DateiManager.config.getTutorial());
		});

	}

	private void addComponents() {
		add(bStart);
		add(cTutorial);
		add(bKonfig);
		add(bEnde);
	}

	public static StartPanel gibInstanz() {
		if (instanz == null)
			instanz = new StartPanel();
		return instanz;
	}

	@Override
	public String gibName() {
		return "Start";
	}

}
