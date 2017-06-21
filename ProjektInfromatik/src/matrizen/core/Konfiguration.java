package matrizen.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasse, mit der die Steuerung angepasst werden kann! Hier werden die
 * "KeyCodes" der Java-API Klasse <code>java.awt.event.KeyEvent</code> mit
 * Integern gespeichert Es wird möglicherweise die Möglichkeit hinzugefügt,
 * eigene Konfigurationen zu erstellen!
 * 
 * @author Stefan
 */
public class Konfiguration {
	private int[] rechts, links, oben, unten, schuss, waffe;
	private List<File> aktiveMusik, inaktiveMusik;
	private File grafiken;
	private short tutorial;

	public Konfiguration() {
		rechts = new int[2];
		links = new int[2];
		oben = new int[2];
		unten = new int[2];
		schuss = new int[2];
		waffe = new int[2];
		aktiveMusik = inaktiveMusik = new ArrayList<File>();
	}

	public Konfiguration setRechts(int i0, int i1) {
		rechts[0] = i0;
		rechts[1] = i1;
		return this;
	}

	public Konfiguration setLinks(int i0, int i1) {
		links[0] = i0;
		links[1] = i1;
		return this;
	}

	public Konfiguration setOben(int i0, int i1) {
		oben[0] = i0;
		oben[1] = i1;
		return this;
	}

	public Konfiguration setUnten(int i0, int i1) {
		unten[0] = i0;
		unten[1] = i1;
		return this;
	}

	public Konfiguration setSchuss(int i0, int i1) {
		schuss[0] = i0;
		schuss[1] = i1;
		return this;
	}

	public Konfiguration setWaffe(int i0, int i1) {
		waffe[0] = i0;
		waffe[1] = i1;
		return this;
	}

	public int[] getRechts() {
		return rechts;
	}

	public Konfiguration setRechts(int[] rechts) {
		this.rechts = rechts;
		return this;
	}

	public int[] getLinks() {
		return links;
	}

	public Konfiguration setLinks(int[] links) {
		this.links = links;
		return this;
	}

	public int[] getOben() {
		return oben;
	}

	public Konfiguration setOben(int[] oben) {
		this.oben = oben;
		return this;
	}

	public int[] getUnten() {
		return unten;
	}

	public Konfiguration setUnten(int[] unten) {
		this.unten = unten;
		return this;
	}

	public int[] getWaffe() {
		return waffe;
	}

	public Konfiguration setWaffe(int[] waffe) {
		this.waffe = waffe;
		return this;
	}

	public int[] getSchuss() {
		return schuss;
	}

	public Konfiguration setSchuss(int[] schuss) {
		this.schuss = schuss;
		return this;
	}

	public List<File> getAktiveMusik() {
		return aktiveMusik;
	}

	public Konfiguration setAktiveMusik(List<File> aktiveMusik) {
		this.aktiveMusik = aktiveMusik;
		return this;
	}

	public List<File> getInaktiveMusik() {
		return inaktiveMusik;
	}

	public Konfiguration setInaktiveMusik(List<File> inaktiveMusik) {
		this.inaktiveMusik = inaktiveMusik;
		return this;
	}

	public File getGrafiken() {
		return grafiken;
	}

	public Konfiguration setGrafiken(File grafiken) {
		this.grafiken = grafiken;
		return this;
	}

	public Konfiguration setTutorial(short tutorial) {
		this.tutorial = tutorial;
		return this;
	}

	public short getTutorial() {
		return tutorial;
	}

}
