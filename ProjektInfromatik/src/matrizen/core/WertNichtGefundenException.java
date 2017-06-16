package matrizen.core;

public class WertNichtGefundenException extends RuntimeException {
	private static final long serialVersionUID = -7471475953599841856L;

	public WertNichtGefundenException(String s) {
		super("Wert [" + s + "] nicht gefunfen");
	}
}
