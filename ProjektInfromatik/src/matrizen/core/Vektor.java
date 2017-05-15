package matrizen.core;

import static matrizen.core.Vektor.Rechenmethode.*;

/**
 * Diese elementare Klasse wird benutzt, um Positionen, Bewegungen oder Verschiebungen darzustellen
* @author Steve
*/
public class Vektor {
	public static final Vektor nullVektor = new Vektor(0, 0);
	
	private float x, y;
	
	public Vektor() {
		this(0, 0);
	}
	
	public Vektor(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vektor add(Vektor v, Rechenmethode r) {
		if(r == kopieren)
			return new Vektor(v.x + x, v.y + y);
		else if(r == hinzufuegen)
			return add(v);
		return null;
	}
	
	public Vektor sub(Vektor v, Rechenmethode r) {
		if(r == kopieren)
			return new Vektor(v.x - x, v.y - y);
		else if(r == hinzufuegen)
			return sub(v);
		return null;
	}
	
	public Vektor add(float x, float y) {
		return addieren(x, y);
	}
	
	public Vektor add(Vektor v) {
		return add(v.x, v.y);
	}
	
	public Vektor addieren(float x, float y) {
		this.x += x;
		this.y += y;
		return this;
	}
	
	public Vektor addieren(Vektor k) {
		return add(k);
	}
	
	public Vektor sub(float x, float y) {
		return subtrahieren(x, y);
	}
	
	public Vektor subtrahieren(float x, float y) {
		return addieren(-x, -y);
	}
	
	public Vektor sub(Vektor k) {
		return sub(k.x, k.y);
	}
	
	public Vektor subtrahieren(Vektor k) {
		return sub(k);
	}
	
	public Vektor mult(float m) {
		return multiplizieren(m);
	}
	
	public Vektor mult(float m, Rechenmethode r) {
		if(r == kopieren)
			return new Vektor(x * m, y * m);
		else if(r == hinzufuegen)
			return mult(m);
		return null;
	}
	
	public Vektor multiplizieren(float m) {
		this.x *= m;
		this.y *= m;
		return this;
	}
	
	public Vektor div(float m) {
		return dividieren(m);
	}
	
	public Vektor dividieren(float m) {
		this.x /= m;
		this.y /= m;
		return this;
	}
	
	public float mag() {
		return (float) Math.sqrt(x*x+y*y);
	}
	
	public String toString() {
		return "Vektor:{x=" + x + ";y=" + y + "}";
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public enum Rechenmethode {
		kopieren, hinzufuegen;
	}
	
}
