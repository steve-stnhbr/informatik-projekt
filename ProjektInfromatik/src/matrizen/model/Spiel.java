package matrizen.model;

public class Spiel {
	public static final short zeilen = (short) 15, spalten = (short) 15;
	private static Spiel instanz;
	private Level level;
	
	private Spiel() {
		
	}
	
	public static Spiel gibInstanz() {
		if(instanz == null)
			instanz = new Spiel();
		return instanz;
	}
	
	public static void main(String[] args) {
		
	}
}
