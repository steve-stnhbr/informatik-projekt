package matrizen.vorhinein;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;

public class EinstellungsPanel extends JPanel implements Benennbar {
	private static final long serialVersionUID = 572488133142487870L;
	static private EinstellungsPanel instanz;

	private JButton bMusik, bEingabe, bGrafiken, bStandard, bZurueck;

	public EinstellungsPanel() {
		initComponents();
		initListeners();
		addComponents();

		setPreferredSize(new Dimension(208, 215));

		setVisible(true);
	}

	private void initComponents() {
		bMusik = new JButton("Musik-Optionen");
		bEingabe = new JButton("Eingabe verwalten");
		bGrafiken = new JButton("Grafiken ändern");
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

		bZurueck.addActionListener((e) -> {
			AnfangsFenster.gibInstanz().inhaltAendern(StartPanel.gibInstanz());
			AnfangsFenster.gibInstanz().repaint();
		});
	}

	private void addComponents() {
		add(bMusik);
		add(bEingabe);
		add(bGrafiken);
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
