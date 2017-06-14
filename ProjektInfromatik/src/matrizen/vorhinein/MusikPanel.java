package matrizen.vorhinein;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.AbstractListModel;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;

import matrizen.core.DateiManager;
import matrizen.core.MusikPlayer;

public class MusikPanel extends JPanel implements Benennbar {
	private static final long serialVersionUID = -3958065149665042579L;
	private static MusikPanel instanz;

	static public MusikPanel gibInstanz() {
		if (instanz == null)
			instanz = new MusikPanel();
		return instanz;
	}

	private Icon playIcon, pauseIcon;
	private boolean spielt;
	private File lied;
	private JScrollPane sAktiv, sInaktiv;
	private JList<File> aktiv, inaktiv;
	private JButton bLaden, bHinzu, bEntf, bSpielen, bAbbruch, bWeiter;
	private JLabel lAktiv, lInaktiv, lError;

	private MusikPanel() {
		initContent();
		initComponents();
		initListeners();
		addComponents();

		setPreferredSize(new Dimension(523, 257));

		setVisible(true);
	}

	private void initContent() {
		playIcon = new ImageIcon(DateiManager.pfad + "res/grafik/play.ico");
		pauseIcon = new ImageIcon(DateiManager.pfad + "res/grafik/pause.ico");
	}

	private void initComponents() {
		lAktiv = new JLabel("aktive Musik");
		lInaktiv = new JLabel("inaktive Musik");
		lError = new JLabel();
		inaktiv = new JList<>(new MusikListModel(false));
		aktiv = new JList<>(new MusikListModel(true));
		sAktiv = new JScrollPane(aktiv);
		sInaktiv = new JScrollPane(inaktiv);
		bLaden = new JButton("Lieder laden...");
		bHinzu = new JButton("hinzufügen >");
		bEntf = new JButton("< entfernen");
		bSpielen = new JButton(playIcon);
		bAbbruch = new JButton(AnfangsFenster.textAbbruch);
		bWeiter = new JButton(AnfangsFenster.textWeiter);

		aktiv.setSize(new Dimension(aktiv.getSize().width, 100));
		inaktiv.setSize(new Dimension(aktiv.getSize().width, 100));
	}

	private void initListeners() {
		aktiv.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				lied = aktiv.getModel().getElementAt(e.getFirstIndex());
			}
		});

		inaktiv.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				lied = inaktiv.getModel().getElementAt(e.getFirstIndex());
			}
		});

		bSpielen.addActionListener((e) -> {
			if (!spielt) {
				MusikPlayer.stop();
				bSpielen.setIcon(playIcon);
			} else {
				if (lied != null) {
					try {
						MusikPlayer.naechsterTitel(AudioSystem.getAudioInputStream(lied));
					} catch (UnsupportedAudioFileException | IOException e1) {
						e1.printStackTrace();
					}

					MusikPlayer.naechstesLied();
					MusikPlayer.abspielen();
					bSpielen.setIcon(pauseIcon);
				}
			}
		});

		bHinzu.addActionListener((e) -> {
			((MusikListModel) aktiv.getModel())
					.add(((MusikListModel) inaktiv.getModel()).getRealFile(inaktiv.getSelectedIndex()));
			((MusikListModel) inaktiv.getModel())
					.remove(((MusikListModel) inaktiv.getModel()).getRealFile(inaktiv.getSelectedIndex()));
			aktiv.updateUI();
			inaktiv.updateUI();
			repaint();
		});

		bEntf.addActionListener((e) -> {
			if (aktiv.getSelectedValue() != null) {
				((MusikListModel) inaktiv.getModel())
						.add(((MusikListModel) aktiv.getModel()).getRealFile(aktiv.getSelectedIndex()));
				((MusikListModel) aktiv.getModel())
						.remove(((MusikListModel) aktiv.getModel()).getRealFile(aktiv.getSelectedIndex()));
			} else {
				((MusikListModel) inaktiv.getModel())
						.remove(((MusikListModel) inaktiv.getModel()).getRealFile(inaktiv.getSelectedIndex()));
			}
			aktiv.updateUI();
			inaktiv.updateUI();
			repaint();
		});

		bAbbruch.addActionListener((e) -> {
			AnfangsFenster.gibInstanz().inhaltAendern(EinstellungsPanel.gibInstanz());

			instanz = null;
		});

		bWeiter.addActionListener((e) -> {
			System.out.println(getWidth() + ":" + getHeight());
			DateiManager.config.setAktiveMusik(((MusikListModel) aktiv.getModel()).getListe());
			DateiManager.config.setInaktiveMusik(((MusikListModel) inaktiv.getModel()).getListe());
			AnfangsFenster.gibInstanz().inhaltAendern(EinstellungsPanel.gibInstanz());
		});

		bLaden.addActionListener((e) -> {
			JFileChooser j = new JFileChooser();
			j.setFileFilter(new FileFilter() {

				@Override
				public String getDescription() {
					return ".wav-Dateien";
				}

				@Override
				public boolean accept(File f) {
					if (f.getName().endsWith(".wav"))
						return true;
					return false;
				}
			});

			j.setFileSelectionMode(JFileChooser.FILES_ONLY);
			j.setMultiSelectionEnabled(true);

			int t = j.showOpenDialog(this);

			if (t == JFileChooser.APPROVE_OPTION)
				((MusikListModel) inaktiv.getModel()).addAll(j.getSelectedFiles());

			inaktiv.updateUI();
		});

		inaktiv.addComponentListener(new ComponentListener() {

			public void componentShown(ComponentEvent e) {
			}

			@Override
			public void componentResized(ComponentEvent e) {
				inaktiv.setSize(new Dimension(inaktiv.getSize().width + 15, inaktiv.getSize().height));
			}

			public void componentMoved(ComponentEvent e) {
			}

			public void componentHidden(ComponentEvent e) {
			}
		});

		aktiv.addComponentListener(new ComponentListener() {

			public void componentShown(ComponentEvent e) {
			}

			@Override
			public void componentResized(ComponentEvent e) {
				aktiv.setSize(new Dimension(aktiv.getSize().width + 15, aktiv.getSize().height));
			}

			public void componentMoved(ComponentEvent e) {
			}

			public void componentHidden(ComponentEvent e) {
			}
		});
	}

	private void addComponents() {
		JPanel pListe = new JPanel(), pAktiv = new JPanel(), pInaktiv = new JPanel(), pButtons = new JPanel();
		pAktiv.setLayout(new BoxLayout(pAktiv, BoxLayout.Y_AXIS));
		pInaktiv.setLayout(new BoxLayout(pInaktiv, BoxLayout.Y_AXIS));
		pButtons.setLayout(new FlowLayout());
		GridLayout l = new GridLayout(3, 1);
		l.setVgap(5);
		pListe.setLayout(l);

		pAktiv.add(lAktiv);
		pAktiv.add(sAktiv);

		pInaktiv.add(lInaktiv);
		pInaktiv.add(sInaktiv);

		pButtons.add(bLaden);
		pButtons.add(bAbbruch);
		pButtons.add(bWeiter);

		pListe.add(bHinzu);
		pListe.add(bEntf);
		pListe.add(lError);

		add(pInaktiv);
		add(pListe);
		add(pAktiv);
		add(pButtons);
	}

	public void standardZuruecksetzen() {

	}

	public void standard() {
		List<File> l = new ArrayList<>();

	}

	class MusikListModel extends AbstractListModel<File> {
		private static final long serialVersionUID = 5196993202984803532L;

		private boolean aktiv;
		private List<File> liste;

		public MusikListModel(boolean aktiv) {
			this.aktiv = aktiv;

			initListe();
		}

		public void update() {
			initListe();
		}

		public List<File> getListe() {
			return liste;
		}

		private void initListe() {
			if (aktiv)
				liste = DateiManager.Musik.aktiveLaden();
			else
				liste = DateiManager.Musik.inaktiveLaden();
		}

		@Override
		public int getSize() {
			return liste.size();
		}

		@Override
		public File getElementAt(int index) {
			return new File(liste.get(index).getName());
		}

		public File getRealFile(int index) {
			return liste.get(index);
		}

		public void add(File f) {
			liste.add(f);
		}

		public void addAll(File... f) {
			liste.addAll(Arrays.<File>asList(f));
		}

		public void remove(File f) {
			liste.remove(f);
		}
	}

	@Override
	public String gibName() {
		return "Musikeinstellungen";
	}
}
