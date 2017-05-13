package matrizen.core;

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

import matrizen.model.Feld;

public class DateiManager {
	private static BufferedImage srcFeld, srcElement, srcPartikel;
	private final static String pfad = ("/"
			+ DateiManager.class.getProtectionDomain().getCodeSource().getLocation().toString().replace("/file:/", ""));

	public static matrizen.model.Level laden(Level l) {
		return LevelParser.parse(l.src);
	}

	public static BufferedImage laden(Bild b) {
		int hoehe = b.src.getHeight() / 7, breite = b.src.getWidth() / 4;
		return b.src.getSubimage(b.x, b.y, hoehe, breite);
	}

	public static String inhaltLesen(String s) {
		try {
			return inhaltLesen(new File(DateiManager.class.getResource(s).toString()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String inhaltLesen(File f) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(f));
		StringBuilder builder = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		reader.close();
		return builder.toString();
	}

	static {
		try {
			if (srcFeld == null)
				srcFeld = ImageIO.read(new File(pfad + "res/grafik/feld_res.png"));
			if (srcElement == null)
				srcElement = ImageIO.read(DateiManager.class.getResource("res\\grafik\\element_res.png"));
			if (srcPartikel == null)
				srcPartikel = ImageIO.read(DateiManager.class.getResource("res\\grafik\\partikel_res.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public enum Level {
		level1(inhaltLesen("res\\levels\\level1.mld")), level2(inhaltLesen("res\\levels\\level2.mld")), level3(
				inhaltLesen("res\\levels\\level3.mld")), level4(inhaltLesen("res\\levels\\level4.mld")), level5(
						inhaltLesen("res\\levels\\level5.mld")), level6(inhaltLesen("res\\levels\\level6.mld"));

		public String src;

		Level(String src) {
			this.src = src;
		}
	}

	public enum Bild {
		feldStein0(0, 0, srcFeld), feldStein1(0, 32, srcFeld), feldStein2(0, 64, srcFeld), feldStein3(0, 96,
				srcFeld), feldGras0(32, 0, srcFeld), feldGras1(32, 32, srcFeld), feldGras2(32, 64, srcFeld), feldGras3(
						32, 96,
						srcFeld), feldSteinchen0(64, 0, srcFeld), feldSteinchen1(64, 32, srcFeld), feldSteinchen2(64,
								64,
								srcFeld), feldSteinchen4(64, 96, srcFeld), feldSchotter0(96, 0, srcFeld), feldSchotter1(
										96, 32, srcFeld), feldSchotter2(96, 64, srcFeld), feldSchotter3(96, 96,
												srcFeld), feldBaum0(128, 0, srcFeld), feldBaum1(128, 32,
														srcFeld), feldWasser(128, 64, srcFeld), feldBruecke(128, 96,
																srcFeld), feldErde0(160, 0, srcFeld), feldErde1(160, 32,
																		srcFeld), feldErde2(160, 64,
																				srcFeld), feldErde3(160, 96,
																						srcFeld), elementSpieler(0, 0,
																								srcElement), elementSchluessel(
																										0, 32,
																										srcElement);

		public int x, y;
		public BufferedImage src;

		private Bild(int x, int y, BufferedImage image) {
			this.x = x;
			this.y = y;
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

		public static Bild zufaelligeErde() {
			return values()[Math.round(Utils.random(20, 24))];
		}

		public static Bild zufaelligeGrafik(Feld.Typ t) {
			switch (t) {
			case WASSER:
				return feldWasser;
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
			case ERDE:
				return zufaelligeErde();
			default:
				return null;
			}
		}
	}

	public static class LevelParser {
		public static matrizen.model.Level parse(String s) {
			JSONObject obj = new JSONObject(s);
			JSONArray arr = obj.getJSONArray("felder");
			Feld[][] felder = new Feld[obj.getInt("hoehe")][obj.getInt("breite")];

			for (int i = 0; i < arr.length(); i++) {
				JSONArray inArr = arr.getJSONArray(i);

				for (int j = 0; j < inArr.length(); j++) {
					felder[i][j] = new Feld(Feld.Typ.gibTyp(inArr.getInt(j)));
				}
			}

			return new matrizen.model.Level(felder);
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
