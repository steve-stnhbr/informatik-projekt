package matrizen.view.einstellung;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;

import matrizen.core.DateiManager;

public class EinstellungsPanel extends JPanel implements Benennbar {
	private static final long serialVersionUID = 572488133142487870L;
	static private EinstellungsPanel instanz;

	private JButton bMusik, bEingabe, bGrafiken, bWerte, bStandard, bZurueck;

	public EinstellungsPanel() {
		initComponents();
		initListeners();
		addComponents();

		setPreferredSize(new Dimension(208, 238));

		setVisible(true);
	}

	private void initComponents() {
		bMusik = new JButton("Musik-Optionen");
		bEingabe = new JButton("Eingabe verwalten");
		bGrafiken = new JButton("Grafiken ändern");
		bWerte = new JButton("Werte bearbeiten");
		bStandard = new JButton("Standard zurücksetzen");
		bZurueck = new JButton("zurück");
	}

	private void initListeners() {
		bMusik.addActionListener((e) -> {
			AnfangsFenster.gibInstanz().inhaltAendern(MusikPanel.gibInstanz());
			AnfangsFenster.gibInstanz().repaint();
		});

		bEingabe.addActionListener((e) -> {
			AnfangsFenster.gibInstanz().inhaltAendern(EingabePanel.gibInstanz());
			AnfangsFenster.gibInstanz().repaint();
		});

		bGrafiken.addActionListener((e) -> {
			AnfangsFenster.gibInstanz().inhaltAendern(GrafikPanel.gibInstanz());
			AnfangsFenster.gibInstanz().repaint();
		});

		bWerte.addActionListener((e) -> {
			AnfangsFenster.gibInstanz().inhaltAendern(WertePanel.gibInstanz());
		});

		bZurueck.addActionListener((e) -> {
			AnfangsFenster.gibInstanz().inhaltAendern(StartPanel.gibInstanz());
			AnfangsFenster.gibInstanz().repaint();
		});
		
		bStandard.addActionListener((e) -> {
			DateiManager.standardConfig();
		});
	}

	private void addComponents() {
		add(bMusik);
		add(bEingabe);
		add(bGrafiken);
		add(bWerte);
		add(bStandard);
		add(bZurueck);
	}

	public static EinstellungsPanel gibInstanz() {
		if (instanz == null)
			instanz = new EinstellungsPanel();
		return instanz;
	}

	@Override
	public String gibName() {
		return "Einstellung auswählen";
	}
}
