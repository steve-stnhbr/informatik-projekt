package matrizen.model;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public abstract class Grafikbasis {
	protected BufferedImage grafik;
	protected BufferedImage[] animation;
	
	public abstract void zeichnen(Graphics2D g);
}
