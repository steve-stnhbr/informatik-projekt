package matrizen.core;

import matrizen.model.Spiel;

public class EingabeManager {
	private static boolean[] eingabe;

	public static void aktivieren(int i) {
		eingabe[i] = true;
	}

	public static void deaktivieren(int i) {
		eingabe[i] = false;
	}

	public static boolean istAktiv(int i) {
		return eingabe[i];
	}

	public static boolean[] gibEingaben() {
		return eingabe;
	}	

	static {		
		if (eingabe == null)
			eingabe = new boolean[5];
	}
}
