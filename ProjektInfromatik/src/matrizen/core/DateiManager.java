package matrizen.core;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class DateiManager {

	
	static class Grafik {
		private static BufferedImage srcFeld, srcElement, srcPartikel;
		
		public static Image laden(Bild b) {
			return b.src.getSubimage(b.x, b.y, b.groesse, b.groesse);
		}
		
		static {
			try {
				if(srcFeld == null)
					srcFeld = ImageIO.read(new File("res/feld_res"));
				if(srcElement == null)
					srcElement = ImageIO.read(new File("res/element_res"));
				if(srcPartikel == null)
					srcPartikel = ImageIO.read(new File("res/partikel_res"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		enum Bild {
			feldStein0(0, 0, 32, srcFeld),
			feldStein1(0, 32, 32, srcFeld),
			feldStein2(0, 64, 32, srcFeld),
			feldStein3(0, 96, 32, srcFeld),
			feldGras0(32, 0, 32, srcFeld),
			feldGras1(32, 32, 32, srcFeld),
			feldGras2(32, 64, 32, srcFeld),
			feldGras3(32, 96, 32, srcFeld),
			feldSteinchen0(64, 0, 32, srcFeld),
			feldSteinchen1(64, 32, 32, srcFeld),
			feldSteinchen2(64, 64, 32, srcFeld),
			feldSteinchen4(64, 96, 32, srcFeld),
			feldSchotter0(96, 0, 32, srcFeld),
			feldSchotter1(96, 32, 32, srcFeld),
			feldSchotter2(96, 64, 32, srcFeld),
			feldSchotter3(96, 96, 32, srcFeld),
			feldBaum0(128, 0, 32, srcFeld),
			feldBaum1(128, 32, 32, srcFeld),
			elementSpieler(0, 0, 32, srcElement),
			elementSchluessel(0, 32, 32, srcElement);
			
			public int x, y, groesse;
			public BufferedImage src;
			
			private Bild(int x, int y, int sc, BufferedImage image) {
				this.x = x;
				this.y = y;
				this.groesse = sc;
				this.src = image;
			}
			
			public Bild zufaelligerStein() {
				return values()[Math.round(Utils.random(4))];
			}
			
			public Bild zufaelligeWiese() {
				return values()[Math.round(Utils.random(4, 8))];
			}
			
			public Bild zufaelligeSteinchen() {
				return values()[Math.round(Utils.random(8, 12))];
			}
			
			public Bild zufaelligerSchotter() {
				return values()[Math.round(Utils.random(12, 16))];
			}
			
			public Bild zufaelligerBaum() {
				return values()[Math.round(Utils.random(16, 18))];
			}
			
		}
	}
}
