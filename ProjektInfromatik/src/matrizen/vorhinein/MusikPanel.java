package matrizen.vorhinein;

import java.io.File;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;

import matrizen.core.DateiManager;

public class MusikPanel extends JPanel {
	private static final long serialVersionUID = -3958065149665042579L;
	private static MusikPanel instanz;

	static public MusikPanel gibInstanz() {
		if (instanz == null)
			instanz = new MusikPanel();
		return instanz;
	}

	private JList<File> inaktiv, aktiv;
	private JButton bHinzu, bEntf, bAbbruch, bWeiter;

	private MusikPanel() {
		initComponents();
		initListeners();
		addComponents();

		setVisible(true);
	}

	private void initComponents() {
		inaktiv = new JList<>(new MusikListModel(false));
		aktiv = new JList<>(new MusikListModel(true));
	}

	private void initListeners() {

	}

	private void addComponents() {

	}

	class MusikListModel extends AbstractListModel<File> {
		private static final long serialVersionUID = 5196993202984803532L;

		private boolean aktiv;
		private List<File> liste;

		public MusikListModel(boolean aktiv) {
			this.aktiv = aktiv;

			initListe();
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
			return liste.get(index);
		}

	}
}
