package matrizen.core;

import static matrizen.core.Utils.random;
import static matrizen.view.SpielFenster.logger;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONObject;

import matrizen.core.DateiManager.Level;
import matrizen.model.Feld;
import matrizen.model.Feld.Typ;
import matrizen.model.elemente.Item.ItemTyp;

/**
 * Diese Klasse reguliert alle Zugriffe auf Dateien
 * @author Stefan
 *
 */
public class DateiManager {
	private static BufferedImage srcFeld, srcElement, srcPartikel;
	public final static String pfad = DateiManager.class.getProtectionDomain().getCodeSource().getLocation().toString()
			.replace("/file:/", "").replace("file: /", "").replace("file:/", "");

	public static matrizen.model.Level laden(Level l) {
		return LevelParser.parse(l.src);
	}

	public static BufferedImage laden(Bild b) {
		int hoehe = b.src.getHeight() / 7, breite = b.src.getWidth() / 4;
		logger.log(java.util.logging.Level.FINEST, "Bild " + b + " von Datei " + b.src + " geladen");
		return b.src.getSubimage(b.x, b.y, breite, hoehe);
	}

	public static String inhaltLesen(String s) {
		try {
			return inhaltLesen(new File(pfad + "/res/" + s));
		} catch (IOException e) {
			logger.log(java.util.logging.Level.SEVERE, e.getMessage(), e);
		}
		return null;
	}

	public static String inhaltLesen(File f) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(f));
		StringBuilder builder = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
			builder.append(System.lineSeparator());
		}
		
		logger.log(java.util.logging.Level.FINEST, "Inhalt: " + builder.toString() + " aus Datei " + f + " gelesen");
		reader.close();
		return builder.toString();
	}

	static {
		try {
			if (srcFeld == null)
				srcFeld = ImageIO.read(new File(pfad + "res/grafik/feld_res.png"));
			/*
			 * if (srcElement == null) srcElement = ImageIO.read(new File(pfad +
			 * "res/grafik/element_res.png")); if (srcPartikel == null)
			 * srcPartikel = ImageIO.read(new File(pfad +
			 * "res/grafik/partikel_res.png"));
			 */
		} catch (IOException e) {
			logger.log(java.util.logging.Level.SEVERE, e.getMessage(), e);
		}
	}

	public enum Level {
		level1(inhaltLesen("/levels/level1.mld")),
		level2(inhaltLesen("/levels/level2.mld")),
		level3(inhaltLesen("/levels/level3.mld")),
		level4(inhaltLesen("/levels/level4.mld")),
		level5(inhaltLesen("/levels/level5.mld")),
		level6(inhaltLesen("/levels/level6.mld"));

		public String src;

		Level(String src) {
			this.src = src;
		}
	}

	public enum Bild {
		feldGras0(0, 0, srcFeld, Typ.WIESE),
		feldGras1(32, 0, srcFeld, Typ.WIESE),
		feldGras2(64, 0, srcFeld, Typ.WIESE),
		feldGras3(96, 0, srcFeld, Typ.WIESE),
		feldStein0(0, 32, srcFeld, Typ.STEIN),
		feldStein1(32, 32, srcFeld, Typ.STEIN),
		feldStein2(64, 32, srcFeld, Typ.STEIN),
		feldStein3(96, 32, srcFeld, Typ.STEIN),
		feldSteinchen0(0, 64, srcFeld, Typ.STEINCHEN),
		feldSteinchen1(32, 64, srcFeld, Typ.STEINCHEN),
		feldSteinchen2(64, 64, srcFeld, Typ.STEINCHEN),
		feldSteinchen4(96, 64, srcFeld, Typ.STEINCHEN),
		feldSchotter0(0, 96, srcFeld, Typ.SCHOTTER),
		feldSchotter1(32, 96, srcFeld, Typ.SCHOTTER),
		feldSchotter2(64, 96, srcFeld, Typ.SCHOTTER),
		feldSchotter3(96, 96, srcFeld, Typ.SCHOTTER),
		feldBaum0(0, 128, srcFeld, Typ.BAUM),
		feldBaum1(32, 128, srcFeld, Typ.BAUM),
		feldWasser(64, 128, srcFeld, Typ.WASSER),
		feldBruecke(96, 128, srcFeld, Typ.BRUECKE),
		feldErde0(0, 160, srcFeld, Typ.ERDE),
		feldErde1(32, 160, srcFeld, Typ.ERDE),
		feldErde2(64, 160, srcFeld, Typ.ERDE),
		feldErde3(96, 160, srcFeld, Typ.ERDE),
		elementSpieler(0, 0, srcElement, null),
		elementSchluessel(32, 0, srcElement, null);

		public int x, y;
		public BufferedImage src;
		public Typ t;

		private Bild(int x, int y, BufferedImage image, Typ typ) {
			this.x = x;
			this.y = y;
			this.src = image;
			this.t = typ;
		}

		public static Bild zufaelligerStein() {
			return values()[random(0, 4)];
		}

		public static Bild zufaelligeWiese() {
			return values()[random(4, 8)];
		}

		public static Bild zufaelligeSteinchen() {
			return values()[random(8, 12)];
		}

		public static Bild zufaelligerSchotter() {
			return values()[random(12, 16)];
		}

		public static Bild zufaelligerBaum() {
			return values()[random(16, 18)];
		}

		public static Bild zufaelligeErde() {
			return values()[random(20, 24)];
		}

		public static Bild zufaelligeGrafik(Feld.Typ t) {
			switch (t) {
			case WASSER:
				return feldWasser;
			case WIESE:
				return feldGras0;
			// return zufaelligeWiese();
			case BAUM:
				return feldBaum0;
			// return zufaelligerBaum();
			case STEINCHEN:
				return feldSteinchen0;
			// return zufaelligeSteinchen();
			case SCHOTTER:
				return feldSchotter0;
			// return zufaelligerSchotter();
			case STEIN:
				return feldStein0;
			// return zufaelligerStein();
			case ERDE:
				return feldErde0;
			// return zufaelligeErde();
			default:
				return null;
			}
		}

		public static Bild gegenstandLaden(ItemTyp t2) {
			return null;
		}
	}

	public static class LevelParser {
		public static matrizen.model.Level parse(String s) {
			JSONObject obj = new JSONObject(s);
			JSONArray arr = obj.getJSONArray("felder");
			Feld[][] felder = new Feld[arr.length()][arr.getJSONArray(0).length()];

			for (int i = 0; i < arr.length(); i++) {
				JSONArray inArr = arr.getJSONArray(i);

				for (int j = 0; j < inArr.length(); j++) {
					felder[j][i] = new Feld(Feld.Typ.gibTyp(inArr.getInt(j)), new Vektor(j, i));
				}
			}
			matrizen.model.Level lvl = new matrizen.model.Level(felder);
			logger.log(java.util.logging.Level.FINEST, "Level " + lvl + " aus " + s + " gelesen");
			return lvl;
		}

		public static String schreiben(matrizen.model.Level l) {
			Feld[][] felder = l.getFelder();
			JSONObject obj = new JSONObject();
			JSONArray arr = new JSONArray();

			for (int i = 0; i < felder.length; i++) {
				for (int j = 0; j < felder[i].length; j++) {
					arr.put(Feld.Typ.gibIndex(felder[i][j].getTyp()));
				}
			}

			obj.put("felder", arr);

			return obj.toString();
		}
	}

	public static class ConfigParser {
		public static List<Konfiguration> parse(String str) {
			List<Konfiguration> list = new ArrayList<Konfiguration>();
			JSONObject obj = new JSONObject(str);
			JSONArray arr = obj.getJSONArray("configs");

			for (int i = 0; i < arr.length(); i++) {
				JSONObject ob = arr.getJSONObject(i);

				list.add(new Konfiguration(ob.getInt("oben"), ob.getInt("rechts"), ob.getInt("unten"),
						ob.getInt("links"), ob.getInt("schuss")));
			}

			logger.log(java.util.logging.Level.FINEST, "Konfigurationen " + list + " aus " + str + " ausgelesen");

			return list;
		}

		public static String write(List<Konfiguration> l) {
			JSONObject obj = new JSONObject();
			JSONArray arr = new JSONArray();

			for (Konfiguration k : l) {
				arr.put(new JSONObject().put("oben", k.getOben()).put("rechts", k.getRechts())
						.put("unten", k.getUnten()).put("links", k.getLinks()).put("schuss", k.getSchuss()));
			}

			return obj.toString();
		}

	}

}
