package matrizen.view;

public class HUD {
	private static HUD instanz;
	
	private HUD() {
		
	}
	
	public static HUD gibInstanz() {
		if(instanz == null)
			instanz = new HUD();
		return instanz;
	}
	
}
