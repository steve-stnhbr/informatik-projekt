package matrizen.core;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class DateiManager {

	
	static class Grafik {
		private static BufferedImage src;
		
		public static Image laden(Bild b) {
			return src.getSubimage(b.x, b.y, 32, 32);
		}
		
		static {
			try {
				src = ImageIO.read(new File("res/feld_res"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		enum Bild {
			feldStein0(0, 0),
			feldStein1(0, 32),
			feldStein2(0, 64),
			feldStein3(0, 96),
			feldGras0(32, 0),
			feldGras1(32, 32),
			feldGras2(32, 64),
			feldGras3(32, 96),
			feldSteinchen0(64, 0),
			feldSteinchen1(64, 32),
			feldSteinchen2(64, 64),
			feldSteinchen4(64, 96),
			feldSchotter0(96, 0),
			feldSchotter1(96, 32),
			feldSchotter2(96, 64),
			feldSchotter3(96, 96),
			feldBaum0(128, 0),
			feldBaum1(128, 32),
			elementSpieler(160, 0);
			
			public int x, y;
			
			private Bild(int x, int y) {
				this.x = x;
				this.y = y;
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
