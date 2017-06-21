package matrizen.view.einstellung;

import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import matrizen.core.DateiManager;
import matrizen.view.SpielFenster;

public class AnfangsFenster extends JFrame {
	private static final long serialVersionUID = 3907609549112425887L;
	public static final String textWeiter = "bestätigen", textAbbruch = "abbrechen";
	private static AnfangsFenster instanz;
	private JPanel inhalt;

	private AnfangsFenster() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addListeners();
		setResizable(false);
		setIconImage(DateiManager.iconLaden());
		setVisible(true);
	}

	private void addListeners() {
		addWindowListener(new WindowListener() {

			public void windowOpened(WindowEvent e) {
			}

			public void windowIconified(WindowEvent e) {
			}

			public void windowDeiconified(WindowEvent e) {
			}

			public void windowDeactivated(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
				DateiManager.configSchreiben();
			}

			public void windowClosed(WindowEvent e) {
			}

			public void windowActivated(WindowEvent e) {
			}
		});
	}

	public JPanel getInhalt() {
		return inhalt;
	}

	public void setInhalt(JPanel inhalt) {
		this.inhalt = inhalt;
		setContentPane(inhalt);
	}

	public static AnfangsFenster gibInstanz() {
		return instanz;
	}

	public static void init() {
		instanz = new AnfangsFenster();
		instanz.inhaltAendern(StartPanel.gibInstanz());
	}

	public static void main(String[] args) {
		try {
			// ändert die formatierung der Konsole, des logs
			// ---------------------------------------------------- zeit, ms,
			// level,
			// nachricht, neue zeile
			System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tT:%tL-%4$s: %5$s%6$s%n");
			SpielFenster.logger.setLevel(Level.FINEST);
			Handler handler = new ConsoleHandler();
			handler.setLevel(Level.FINEST);

			if (!new File(System.getenv("APDATA") + "/Trollkarl/.m").exists()) {
				DateiManager.initConfig();
				try {
					new File(System.getenv("APDATA") + "/Trollkarl").mkdirs();
					new File(System.getenv("APDATA") + "/Trollkarl/.m").createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}

				DateiManager.init = true;
			} else {
				DateiManager.init = true;
			}

			SpielFenster.logger.addHandler(handler);
			SpielFenster.init();
			init();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void inhaltAendern(JPanel p) {
		setInhalt(p);
		repaint();
		setSize(p.getPreferredSize());
		setTitle(((Benennbar) p).gibName());
		setBounds(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - getSize().width / 2,
				Toolkit.getDefaultToolkit().getScreenSize().height / 2 - getSize().height / 2, getSize().width,
				getSize().height);
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}

		setVisible(true);
	}

}
