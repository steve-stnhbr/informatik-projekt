package matrizen.model;

public class Spiel {
	private static Spiel instanz;
	
	private Spiel() {
		
	}
	
	public static Spiel gibInstanz() {
		if(instanz == null)
			instanz = new Spiel();
		return instanz;
	}
	
	public static void main(String[] args){
		
	}
}
