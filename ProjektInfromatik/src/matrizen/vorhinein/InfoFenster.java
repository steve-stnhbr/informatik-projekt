package matrizen.vorhinein;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InfoFenster extends JFrame {
	private static final long serialVersionUID = -6379431883901997995L;

	public InfoFenster(Informierbar i) {
		JLabel label = new JLabel(i.getInfoText());
		JButton button = new JButton("OK!");
		JPanel pane = new JPanel();
		button.addActionListener((e) -> dispose());
		pane.add(label);
		pane.add(button);
		add(pane);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);
	}
}
