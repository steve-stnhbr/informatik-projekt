package matrizen.core;

import static java.awt.event.KeyEvent.*;

public class Konfiguration {
	public static final Konfiguration standardWASD = new Konfiguration(VK_W, VK_D, VK_S, VK_A, VK_SPACE),
			standardPfeil = new Konfiguration(VK_UP, VK_RIGHT, VK_DOWN, VK_LEFT, VK_SPACE);
	private int rechts, links, oben, unten, schuss;

	public Konfiguration() {}

	public Konfiguration(int oben, int rechts, int unten, int links, int schuss) {
		super();
		this.rechts = rechts;
		this.links = links;
		this.oben = oben;
		this.unten = unten;
		this.schuss = schuss;
	}

	public int getRechts() {
		return rechts;
	}

	public void setRechts(int rechts) {
		this.rechts = rechts;
	}

	public int getLinks() {
		return links;
	}

	public void setLinks(int links) {
		this.links = links;
	}

	public int getOben() {
		return oben;
	}

	public void setOben(int oben) {
		this.oben = oben;
	}

	public int getUnten() {
		return unten;
	}

	public void setUnten(int unten) {
		this.unten = unten;
	}

	public int getSchuss() {
		return schuss;
	}

	public void setSchuss(int schuss) {
		this.schuss = schuss;
	}
}
