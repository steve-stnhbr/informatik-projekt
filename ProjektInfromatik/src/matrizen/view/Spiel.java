package matrizen.view;

public class Spiel {
	private static Spiel instanz;
	
	
	public static Spiel gibInstanz() {
		if(instanz == null)
			instanz = new Spiel();
		return instanz;
	}
	
	public static void main(String[] args){
		
	}
}
