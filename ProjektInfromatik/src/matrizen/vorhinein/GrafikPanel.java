package matrizen.vorhinein;

import java.awt.Container;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import matrizen.core.DateiManager;

public class GrafikPanel extends JPanel implements Informierbar {
	private static final long serialVersionUID = 7295990738900352673L;
	private static GrafikPanel instanz;

	private File ordner;
	private JLabel lGElement, lGFigur, lGFeld, lGItem, lGPartikel;
	private JLabel lGrafik, lElement, lFigur, lFeld, lItem, lPartikel;
	private JButton bInfo, bLaden, bAbbruch, bWeiter;

	private GrafikPanel() {
		initContent();
		initComponents();
		initListeners();
		addComponents();
	}

	private void initContent() {
		ordner = DateiManager.config.getGrafiken();
	}

	private void initComponents() {
		lGrafik = new JLabel("f:");
		lElement = new JLabel("Levelelement:");
		lFigur = new JLabel("Figuren:");
		lFeld = new JLabel("Felder:");
		lPartikel = new JLabel("Partikel:");

		try {
			lGElement = new JLabel(new ImageIcon(ImageIO.read(new File(ordner, "element_res.png"))));
			lGFigur = new JLabel(new ImageIcon(ImageIO.read(new File(ordner, "figur_res.png"))));
			lGFeld = new JLabel(new ImageIcon(ImageIO.read(new File(ordner, "feld_res.png"))));
			lGItem = new JLabel(new ImageIcon(ImageIO.read(new File(ordner, "item_res.png"))));
			lGPartikel = new JLabel(new ImageIcon(ImageIO.read(new File(ordner, "partikel_res.png"))));
		} catch (IOException e) {
			e.printStackTrace();
		}

		bInfo = new JButton("Info");
		bLaden = new JButton("Grafiken laden...");
		bAbbruch = new JButton(AnfangsFenster.textAbbruch);
		bWeiter = new JButton(AnfangsFenster.textWeiter);
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
		});
	}

	private void addComponents() {
		add(lGrafik);
		add(lElement);
		add(lFigur);
		add(lFeld);
		add(lItem);
		add(lPartikel);

		add(lGElement);
		add(lGFigur);
		add(lGFeld);
		add(lGItem);
		add(lGPartikel);

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

}
