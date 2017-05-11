package matrizen.view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class SpielFenster extends JFrame {
	public static final int hoehe = 600, breite = 600;
	private static SpielFenster instanz;
	private BufferedImage bImg;
	private Graphics2D graphics;
	
	private SpielFenster() {
		bImg = new BufferedImage(hoehe, breite, BufferedImage.TYPE_INT_RGB);
		graphics = (Graphics2D) bImg.getGraphics();
	}
	
	public static SpielFenster gibInstanz() {
		if(instanz == null)
			instanz = new SpielFenster();
		return instanz;
	}
}
