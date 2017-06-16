package matrizen.view.einstellung;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import matrizen.core.DateiManager;

public class GrafikPanel extends JPanel implements Informierbar, Benennbar {
	private static final long serialVersionUID = 7295990738900352673L;
	private static GrafikPanel instanz;

	private File ordner;
	private JLabel lGFigur, lGFeld, lGItem, lGPartikel;
	private JLabel lFigur, lFeld, lItem, lPartikel;
	private JButton bInfo, bLaden, bAbbruch, bWeiter;

	private GrafikPanel() {
		initContent();
		initComponents();
		initListeners();
		addComponents();

		setPreferredSize(new Dimension(380, 510));

		setVisible(true);
	}

	private void initContent() {
		ordner = DateiManager.config.getGrafiken();
	}

	private void initComponents() {
		initIcons();

		lFigur = new JLabel("Figuren:");
		lFeld = new JLabel("Felder:");
		lItem = new JLabel("Items");
		lPartikel = new JLabel("Partikel:");

		bInfo = new JButton("Info");
		bLaden = new JButton("Grafiken laden...");
		bAbbruch = new JButton(AnfangsFenster.textAbbruch);
		bWeiter = new JButton(AnfangsFenster.textWeiter);
	}

	private void initIcons() {
		try {
			ImageIcon iFigur = new ImageIcon(ImageIO.read(new File(ordner, "figur_res.png"))),
					iFeld = new ImageIcon(ImageIO.read(new File(ordner, "feld_res.png"))),
					iItem = new ImageIcon(ImageIO.read(new File(ordner, "item_res.png"))),
					iPartikel = new ImageIcon(ImageIO.read(new File(ordner, "partikel_res.png")));
			lGFigur = new JLabel(iFigur);
			lGFeld = new JLabel(iFeld);
			lGItem = new JLabel(iItem);
			lGPartikel = new JLabel(iPartikel);

			int w = (getWidth() - 10) / 4, h = w * iItem.getIconHeight() / iFigur.getIconWidth();

			lGFigur.setSize(w, h);
			lGFeld.setSize(w, h);
			lGItem.setSize(w, h);
			lGPartikel.setSize(w, h);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initListeners() {
		bInfo.addActionListener((e) -> new InfoFenster(this));

		bLaden.addActionListener((e) -> {
			JFileChooser f = new JFileChooser();
			f.setFileFilter(new FileFilter() {

				@Override
				public String getDescription() {
					return "Ordner mit Grafik-Dateien";
				}

				@Override
				public boolean accept(File f) {
					if (f.isDirectory() && new File(f, "feld_res.png").exists()
							&& new File(f, "element_res.png").exists() && new File(f, "figur_res.png").exists()
							&& new File(f, "item_res.png").exists() && new File(f, "partikel_res.png").exists())
						return true;
					return false;
				}
			});

			f.setMultiSelectionEnabled(false);

			int t = f.showOpenDialog(this);

			if (t == JFileChooser.APPROVE_OPTION)
				ordner = f.getSelectedFile();
		});

		bAbbruch.addActionListener((e) -> {
			AnfangsFenster.gibInstanz().inhaltAendern(EinstellungsPanel.gibInstanz());

			instanz = null;
		});

		bWeiter.addActionListener((e) -> {
			DateiManager.config.setGrafiken(ordner);
			AnfangsFenster.gibInstanz().inhaltAendern(EinstellungsPanel.gibInstanz());
		});
	}

	private void addComponents() {
		JPanel pItem = new JPanel(), pFigur = new JPanel(), pFeld = new JPanel(), pPartikel = new JPanel();

		pItem.setLayout(new BoxLayout(pItem, BoxLayout.Y_AXIS));
		pFigur.setLayout(new BoxLayout(pFigur, BoxLayout.Y_AXIS));
		pFeld.setLayout(new BoxLayout(pFeld, BoxLayout.Y_AXIS));
		pPartikel.setLayout(new BoxLayout(pPartikel, BoxLayout.Y_AXIS));

		pItem.add(lItem);
		pItem.add(lGItem);

		pFigur.add(lFigur);
		pFigur.add(lGFigur);

		pFeld.add(lFeld);
		pFeld.add(lGFeld);

		pPartikel.add(lPartikel);
		pPartikel.add(lGPartikel);

		add(pItem);
		add(pFigur);
		add(pFeld);
		add(pPartikel);

		add(bLaden);
		add(bAbbruch);
		add(bWeiter);
	}

	public static JPanel gibInstanz() {
		if (instanz == null)
			instanz = new GrafikPanel();
		return instanz;
	}

	@Override
	public String getInfoText() {
		// TODO
		return null;
	}

	public void standard() {
		DateiManager.config.setGrafiken(new File(DateiManager.pfad + "res/grafik"));
	}

	@Override
	public String gibName() {
		return "Grafikeinstellungen";
	}

}
