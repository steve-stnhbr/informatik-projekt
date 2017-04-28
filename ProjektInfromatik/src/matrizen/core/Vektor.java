package matrizen.core;

/**
* @author Steve
*/
public class Vektor {
	private float x, y;
	
	public Vektor() {
		this(0, 0);
	}
	
	public Vektor(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vektor add(float x, float y) {
		return addieren(x, y);
	}
	
	public Vektor addieren(float x, float y) {
		this.x += x;
		this.y += y;
		return this;
	}
	
	public Vektor sub(float x, float y) {
		return subtrahieren(x, y);
	}
	
	public Vektor subtrahieren(float x, float y) {
		return addieren(-x, -y);
	}
	
	public Vektor mult(float m) {
		return multiplizieren(m);
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
	
	//TODO
	
	public float mag() {
		return (float) Math.sqrt(x*x+y*y);
	}	
	
}
