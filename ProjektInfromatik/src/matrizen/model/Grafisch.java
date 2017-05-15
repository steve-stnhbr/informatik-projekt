package matrizen.model;

import java.awt.Graphics2D;
import java.awt.Image;

public abstract class Grafisch {
	protected Image grafik;
	protected Image[] animation;
	
	public abstract void zeichnen(Graphics2D g);
}
