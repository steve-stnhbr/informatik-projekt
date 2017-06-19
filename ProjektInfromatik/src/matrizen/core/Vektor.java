package matrizen.core;

import static matrizen.core.Vektor.Rechenmethode.*;
import static matrizen.view.SpielFenster.logger;

import java.util.logging.Level;

/**
 * Diese elementare Klasse wird benutzt, um Positionen, Bewegungen oder
 * Verschiebungen darzustellen
 * 
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
		if (r == kopieren)
			return new Vektor(v.x + x, v.y + y);
		else if (r == hinzufuegen)
			return add(v);
		return null;
	}

	public Vektor sub(Vektor v, Rechenmethode r) {
		if (r == kopieren)
			return new Vektor(v.x - x, v.y - y);
		else if (r == hinzufuegen)
			return sub(v);
		return null;
	}

	public Vektor add(float x, float y) {
		this.x += x;
		this.y += y;
		return this;
	}

	public Vektor add(Vektor v) {
		x += v.x;
		y += v.y;
		return this;
	}

	public Vektor sub(Vektor v) {
		x -= v.x;
		y -= v.y;
		return this;
	}

	public Vektor sub(float x, float y) {
		this.x -= x;
		this.y -= y;
		return this;
	}

	public Vektor mult(float m) {
		x *= m;
		y *= m;
		return this;
	}

	public Vektor mult(float m, Rechenmethode r) {
		if (r == kopieren)
			return new Vektor(x * m, y * m);
		else if (r == hinzufuegen)
			return mult(m);
		return null;
	}

	public Vektor div(float m) {
		x /= m;
		y /= m;
		return this;
	}

	public float skalar(Vektor o) {
		return (x + o.x) * (y + o.y);
	}

	public float kreuz(Vektor o) {
		return x * o.y - y * o.x;
	}

	public float mag() {
		return (float) Math.sqrt(x * x + y * y);
	}

	public float dist(Vektor o) {
		return (float) Math.sqrt(Math.pow((o.x - x), 2) + Math.pow((o.y - y), 2));
	}

	public Vektor kopieren() {
		return new Vektor(x, y);
	}

	public boolean equals(Vektor o) {
		if (o != null)
			return this.x == o.x && this.y == o.y;
		return false;
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

	public Vektor normalize() {
		div(mag());
		return this;
	}

	public Vektor round() {
		x = (int) x;
		y = (int) y;
		return this;
	}
}
