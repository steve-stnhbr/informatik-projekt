package matrizen.core;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONObject;

import matrizen.model.Feld;

public class DateiManager {
	private static BufferedImage srcFeld, srcElement, srcPartikel;

	public static matrizen.model.Level laden(Level l) {
		return LevelParser.parse(l.src);
	}

	public static BufferedImage laden(Bild b) {
		return b.src.getSubimage(b.x, b.y, b.groesse, b.groesse);
	}

	public static String inhaltLesen(String s) {
		try {
			return inhaltLesen(new File(s));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String inhaltLesen(File f) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String s = "", line = null;
		while ((line = reader.readLine()) != null) {
			s.concat(line + System.lineSeparator());
		}

		reader.close();
		return s;
	}

	static {
		try {
			if (srcFeld == null)
				srcFeld = ImageIO.read(new File("res/feld_res"));
			if (srcElement == null)
				srcElement = ImageIO.read(new File("res/element_res"));
			if (srcPartikel == null)
				srcPartikel = ImageIO.read(new File("res/partikel_res"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	enum Level {
		level1(inhaltLesen("res/levels/level1.mld")), level2(inhaltLesen("res/levels/level2.mld")), level3(
				inhaltLesen("res/levels/level3.mld")), level4(inhaltLesen("res/levels/level4.mld")), level5(
						inhaltLesen("res/levels/level5.mld")), level6(inhaltLesen("res/levels/level6.mld"));

		public String src;

		Level(String src) {
			this.src = src;
		}
	}

	public enum Bild {
		feldStein0(0, 0, 32, srcFeld), feldStein1(0, 32, 32, srcFeld), feldStein2(0, 64, 32, srcFeld), feldStein3(0, 96,
				32, srcFeld), feldGras0(32, 0, 32, srcFeld), feldGras1(32, 32, 32, srcFeld), feldGras2(32, 64, 32,
						srcFeld), feldGras3(32, 96, 32, srcFeld), feldSteinchen0(64, 0, 32, srcFeld), feldSteinchen1(64,
								32, 32, srcFeld), feldSteinchen2(64, 64, 32, srcFeld), feldSteinchen4(64, 96, 32,
										srcFeld), feldSchotter0(96, 0, 32, srcFeld), feldSchotter1(96, 32, 32,
												srcFeld), feldSchotter2(96, 64, 32, srcFeld), feldSchotter3(96, 96, 32,
														srcFeld), feldBaum0(128, 0, 32, srcFeld), feldBaum1(128, 32, 32,
																srcFeld), elementSpieler(0, 0, 32,
																		srcElement), elementSchluessel(0, 32, 32,
																				srcElement);

		public int x, y, groesse;
		public BufferedImage src;

		private Bild(int x, int y, int sc, BufferedImage image) {
			this.x = x;
			this.y = y;
			this.groesse = sc;
			this.src = image;
		}

		public static Bild zufaelligerStein() {
			return values()[Math.round(Utils.random(4))];
		}

		public static Bild zufaelligeWiese() {
			return values()[Math.round(Utils.random(4, 8))];
		}

		public static Bild zufaelligeSteinchen() {
			return values()[Math.round(Utils.random(8, 12))];
		}

		public static Bild zufaelligerSchotter() {
			return values()[Math.round(Utils.random(12, 16))];
		}

		public static Bild zufaelligerBaum() {
			return values()[Math.round(Utils.random(16, 18))];
		}
		
		public static Bild zufaelligesWasser() {
			return values()[Math.round(Utils.random(18, 20))];
		}

		public static Bild zufaelligeGrafik(Feld.Typ t) {
			switch (t) {
			case WASSER:
				return zufaelligesWasser();
			case WIESE:
				return zufaelligeWiese();
			case BAUM:
				return zufaelligerBaum();
			case STEINCHEN:
				return zufaelligeSteinchen();
			case SCHOTTER:
				return zufaelligerSchotter();
			case STEIN:
				return zufaelligerStein();
			default:
				return null;
			}
		}
	}

	private static class LevelParser {
		private static matrizen.model.Level parse(String s) {
			JSONObject obj = new JSONObject(s);
			JSONArray arr = obj.getJSONArray("felder");
			Feld[][] felder = new Feld[obj.getInt("hoehe")][obj.getInt("breite")];

			for (int i = 0; i < arr.length(); i++) {
				JSONArray inArr = arr.getJSONArray(i);

				for (int j = 0; j < inArr.length(); j++) {
					felder[i][j] = new Feld(Feld.Typ.gibTyp(inArr.getInt(j)));
				}
			}

			return null;

		}
	}

}
