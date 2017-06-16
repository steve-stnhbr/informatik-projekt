package matrizen.view.einstellung;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import matrizen.core.DateiManager;

public class EingabePanel extends JPanel implements Benennbar {
	private static final long serialVersionUID = 6479304563663625457L;
	private static EingabePanel instanz;

	private String[] inhalt;
	private HashMap<String, Integer[]> map;

	private JComboBox<String> combo;
	private JTextField fEingabe1, fEingabe2;
	private JLabel lEingabe1, lEingabe2, lCombo;
	private JButton bWeiter, bAbbruch;

	private EingabePanel() {
		initContent();
		initComponents();
		initListeners();
		addComponents();

		setPreferredSize(new Dimension(216, 218));

		setVisible(true);
	}

	private void initContent() {
		map = new HashMap<>();
		inhalt = new String[] { "Bewegung oben", "Bewegung unten", "Bewegung rechts", "Bewegung links", "Schuss" };

		map.put(inhalt[0], new Integer[] { DateiManager.config.getOben()[0], DateiManager.config.getOben()[1] });
		map.put(inhalt[1], new Integer[] { DateiManager.config.getUnten()[0], DateiManager.config.getUnten()[1] });
		map.put(inhalt[2], new Integer[] { DateiManager.config.getRechts()[0], DateiManager.config.getRechts()[1] });
		map.put(inhalt[3], new Integer[] { DateiManager.config.getLinks()[0], DateiManager.config.getLinks()[1] });
		map.put(inhalt[4], new Integer[] { DateiManager.config.getSchuss()[0], DateiManager.config.getSchuss()[1] });
	}

	private void initComponents() {
		lCombo = new JLabel("Steuerungselement auswählen");
		combo = new JComboBox<>(inhalt);
		lEingabe1 = new JLabel("Eingabeoption 1");
		fEingabe1 = new JTextField();
		lEingabe2 = new JLabel("Eingabeoption 2");
		fEingabe2 = new JTextField();
		bWeiter = new JButton(AnfangsFenster.textWeiter);
		bAbbruch = new JButton(AnfangsFenster.textAbbruch);

		fEingabe1.setHorizontalAlignment(JTextField.CENTER);
		fEingabe2.setHorizontalAlignment(JTextField.CENTER);

		int c0 = map.get((String) combo.getSelectedItem())[0], c1 = map.get((String) combo.getSelectedItem())[1];

		setText(fEingabe1, c0);
		setText(fEingabe2, c1);
	}

	private void initListeners() {
		combo.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				int c0 = map.get((String) e.getItem())[0], c1 = map.get((String) e.getItem())[1];

				setText(fEingabe1, c0);
				setText(fEingabe2, c1);
			}
		});

		fEingabe1.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				Integer[] i = map.get(combo.getSelectedItem());
				i[0] = e.getKeyCode();
				map.put((String) combo.getSelectedItem(), i);
				setText(fEingabe1, e.getKeyCode());
				fEingabe1.updateUI();
				AnfangsFenster.gibInstanz().repaint();
				AnfangsFenster.gibInstanz().pack();
			}

			public void keyPressed(KeyEvent e) {
			}
		});

		fEingabe2.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				Integer[] i = map.get(combo.getSelectedItem());
				i[1] = e.getKeyCode();
				map.put((String) combo.getSelectedItem(), i);
				setText(fEingabe2, e.getKeyCode());
				fEingabe2.updateUI();
				AnfangsFenster.gibInstanz().repaint();
				AnfangsFenster.gibInstanz().pack();
			}

			@Override
			public void keyPressed(KeyEvent e) {

			}
		});

		bAbbruch.addActionListener((e) -> {
			AnfangsFenster.gibInstanz().inhaltAendern(EinstellungsPanel.gibInstanz());

			instanz = null;
		});

		bWeiter.addActionListener((e) -> {
			DateiManager.config.setOben(map.get(inhalt[0])[0], map.get(inhalt[0])[1]);
			DateiManager.config.setUnten(map.get(inhalt[1])[0], map.get(inhalt[1])[1]);
			DateiManager.config.setRechts(map.get(inhalt[2])[0], map.get(inhalt[2])[1]);
			DateiManager.config.setLinks(map.get(inhalt[3])[0], map.get(inhalt[3])[1]);
			DateiManager.config.setSchuss(map.get(inhalt[4])[0], map.get(inhalt[4])[1]);

			AnfangsFenster.gibInstanz().inhaltAendern(EinstellungsPanel.gibInstanz());
		});

	}

	private void setText(JTextField t, int i) {
		t.setText(KeyEvent.getKeyText(i).length() == 1 ? ("  " + KeyEvent.getKeyText(i) + "\t")
				: ("  " + KeyEvent.getKeyText(i) + "\t"));
	}

	private void addComponents() {
		add(lCombo);
		add(combo);
		add(lEingabe1);
		add(fEingabe1);
		add(lEingabe2);
		add(fEingabe2);
		add(bAbbruch);
		add(bWeiter);
	}

	public static EingabePanel gibInstanz() {
		if (instanz == null)
			instanz = new EingabePanel();
		return instanz;
	}

	@Override
	public String gibName() {
		return "Steuerungseinstellungen";
	}

}
