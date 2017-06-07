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
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONObject;

import matrizen.model.Feld;
import matrizen.model.Levelelement;
import matrizen.model.elemente.Gegner;
import matrizen.model.elemente.Geschoss;
import matrizen.model.elemente.GrafikTyp;
import matrizen.model.elemente.Item;

/**
 * Diese Klasse reguliert alle Zugriffe auf Dateien
 * 
 * @author Stefan
 *
 */
public class DateiManager {
	private static BufferedImage srcFeld, srcFigur, srcPartikel, srcItem;
	public final static String pfad = DateiManager.class.getProtectionDomain().getCodeSource().getLocation().toString()
			.replace("/file:/", "").replace("file: /", "").replace("file:/", "");

	/**
	 * lässt zu, dass aus der Datei ein Level-Objekt erstellt wird
	 * 
	 * @param l
	 * @return
	 */
	public static matrizen.model.Level laden(Level l) {
		return LevelParser.parse(l.src);
	}

	/**
	 * Durch diese Methode kann aus einer Datei ein Bild geladen werden, das
	 * später auf den Bildschirm gezeichnet wird
	 * 
	 * @param b
	 * @return
	 */
	public static BufferedImage laden(Bild b) {
		int hoehe = b.src.getHeight() / 7, breite = b.src.getWidth() / 4;
		logger.log(java.util.logging.Level.FINEST, "Bild " + b + " von Datei " + b.src + " geladen");
		return b.src.getSubimage(b.x, b.y, breite, hoehe + 5);
	}

	/**
	 * Diese Methode liest den Inhalt einer Datei in einen String ein
	 * 
	 * @param s
	 * @return
	 */
	public static String inhaltLesen(String s) {
		try {
			return inhaltLesen(new File(pfad + "/res/" + s));
		} catch (IOException e) {
			logger.log(java.util.logging.Level.SEVERE, e.getMessage(), e);
		}
		return null;
	}

	/**
	 * In dieser Methode wird ein String aus der übergebenen Datei ausgelesen
	 * 
	 * @param f
	 * @return
	 * @throws IOException
	 */
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
			if (srcFigur == null)
				srcFigur = ImageIO.read(new File(pfad + "res/grafik/figur_res.png"));
			if (srcPartikel == null)
				srcPartikel = ImageIO.read(new File(pfad + "res/grafik/partikel_res.png"));
			if (srcItem == null)
				srcItem = ImageIO.read(new File(pfad + "res/grafik/item_res.png"));
		} catch (IOException e) {
			logger.log(java.util.logging.Level.SEVERE, e.getMessage(), e);
		}
	}

	/**
	 * Enumerations-Klasse, die die Infos für die Level-Dateien enthält
	 * 
	 * @author Stefan
	 *
	 */
	public enum Level {
		level0(inhaltLesen("/levels/level0.mld")),
		level1(inhaltLesen("/levels/level1.mld")),
		level2(inhaltLesen("/levels/level2.mld")),
		level3(inhaltLesen("/levels/level3.mld")),
		level4(inhaltLesen("/levels/level4.mld")),
		level5(inhaltLesen("/levels/level5.mld"));

		public String src;

		Level(String src) {
			this.src = src;
		}
	}

	/**
	 * Enumerations-Klasse, die die Infos der grafischen Elemente enthält
	 */
	public enum Bild {
		nullGrafik(96, 64, srcFeld),
		feldGras0(0, 0, srcFeld),
		feldGras1(32, 0, srcFeld),
		feldGras2(64, 0, srcFeld),
		feldGras3(96, 0, srcFeld),
		feldStein0(0, 32, srcFeld),
		feldStein1(32, 32, srcFeld),
		feldStein2(64, 32, srcFeld),
		feldStein3(96, 32, srcFeld),
		feldSteinchen0(0, 64, srcFeld),
		feldSteinchen1(32, 64, srcFeld),
		feldSchotter0(0, 96, srcFeld),
		feldWeiter(32, 96, srcFeld),
		feldBaum0(0, 128, srcFeld),
		feldBaum1(32, 128, srcFeld),
		feldWasser(64, 128, srcFeld),
		feldBruecke(96, 128, srcFeld),
		feldErde0(0, 160, srcFeld),
		feldErde1(32, 160, srcFeld),
		feldErde2(64, 160, srcFeld),
		feldErde3(96, 160, srcFeld),
		figurSpieler(0, 0, srcFigur),
		figurSpielerAnim0(32, 0, srcFigur),
		figurGegener(0, 128, srcFigur),
		itemSchluessel(0, 0, srcItem),
		itemMuenze(0, 32, srcItem),
		itemHerz(64, 0, srcItem),
		partikelMittelOrange(0, 0, srcPartikel),
		partikelMittelBlau(32, 0, srcPartikel),
		partikelKleinRot(32, 0, srcPartikel),
		partikelSternRotGelb(0, 32, srcPartikel);

		public int x, y;
		public BufferedImage src;

		private Bild(int x, int y, BufferedImage image) {
			this.x = x;
			this.y = y;
			this.src = image;
		}

		public static Bild zufaelligerStein() {
			return values()[random(5, 7)];
		}

		public static Bild zufaelligeWiese() {
			return values()[random(1, 5)];
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

		public static Bild zufaelligeGrafik(GrafikTyp t) {
			if (t instanceof Feld.Typ)
				return zufaelligesFeld((Feld.Typ) t);
			else if (t instanceof Item.Typ)
				return zufaelligesItem((Item.Typ) t);
			else if (t instanceof Geschoss.Typ)
				return zufaelligerPartikel((Geschoss.Typ) t);
			return nullGrafik;
		}

		private static Bild zufaelligerPartikel(Geschoss.Typ t) {
			switch (t) {
			case kleinBlau:
				return partikelMittelBlau;
			case kleinOrange:
				return partikelMittelOrange;
			case stern:
				return partikelSternRotGelb;
			default:
				return nullGrafik;
			}
		}

		private static Bild zufaelligesItem(Item.Typ t) {
			switch (t) {
			case herz: 
				return itemHerz;
			case muenze:
				return itemMuenze;
			case schluessel:
				return itemSchluessel;
			default:
				return nullGrafik;
			}
		}

		public static Bild zufaelligesFeld(Feld.Typ t) {
			switch (t) {
			case WASSER:
				return feldWasser;
			case WIESE:
				return zufaelligeWiese();
			case BAUM:
				return Math.random() > 0.5 ? feldBaum0 : feldBaum1;
			case STEINCHEN:
				return feldSteinchen0;
			case SCHOTTER:
				return feldSchotter0;
			case STEIN:
				return zufaelligerStein();
			case ERDE:
				return feldErde0;
			case WEITER:
				return feldWeiter;
			default:
				return nullGrafik;
			}
		}
	}

	public static class LevelParser {

		/**
		 * Diese Methode liest einen String in ein JSON-Objekt ein, das wiederum
		 * in ein Level-Objekt umgewandelt wird *
		 * 
		 * @param s
		 * @return
		 */
		public static matrizen.model.Level parse(String s) {
			JSONObject obj = new JSONObject(s);
			JSONArray arr = obj.getJSONArray("felder");
			Feld[][] felder = new Feld[arr.getJSONArray(0).length()][arr.length()];

			for (int i = 0; i < arr.length(); i++) {
				JSONArray inArr = arr.getJSONArray(i);

				for (int j = 0; j < inArr.length(); j++) {
					felder[j][i] = new Feld(Feld.Typ.gibTyp(inArr.getInt(j)), new Vektor(j, i));
				}
			}

			arr = obj.getJSONArray("gegner");
			List<Levelelement> elem = new CopyOnWriteArrayList<Levelelement>();

			for (int i = 0; i < arr.length(); i++) {
				JSONObject geg = arr.getJSONObject(i);
				int t = geg.getInt("typ"), x = geg.getInt("x"), y = geg.getInt("y");
				elem.add(Gegner.erstellen(t, x, y));
			}

			arr = obj.getJSONArray("items");

			for (int i = 0; i < arr.length(); i++) {
				JSONObject item = arr.getJSONObject(i);
				int t = item.getInt("typ"), x = item.getInt("x"), y = item.getInt("y");
				elem.add(new Item(Item.Typ.values()[t], new Vektor(x, y)));
			}

			matrizen.model.Level lvl = new matrizen.model.Level(elem, felder);
			logger.log(java.util.logging.Level.FINEST, "Level " + lvl + " aus " + s + " gelesen");
			return lvl;
		}

		/**
		 * Diese Methode lässt zu, dass ein Level in ein JSON-Objekt umgewandelt
		 * wird, das als String zurückgegeben wird *
		 * 
		 * @param l
		 * @return
		 */
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

	/**
	 * Diese Klasse wird nicht unbedingt benötigt. Sie wird nur gebraucht, wenn
	 * die Möglichkeit, verschiedene Tasten der Tastatur verschiedenen Aktionen
	 * zuzuordnen hinzugefügt wird
	 * 
	 * @author Stefan
	 *
	 */
	public static class ConfigParser {
		/**
		 * Diese Methode liest einen String in ein JSON-Objekt, aus dem ein
		 * Konfigurations-Objekt erstellt wird
		 * 
		 * @param str
		 * @return
		 */
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

		/**
		 * Diese Methode schreibt eine Konfiguration in ein JSON-Objekt, das als
		 * String zurückgegeben wird
		 * 
		 * @param l
		 * @return
		 */
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
