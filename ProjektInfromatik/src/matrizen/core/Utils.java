package matrizen.core;

import java.util.Random;

public class Utils {
	private static final Random random = new Random();
	
	public static float random(float end) {
		return random.nextInt((int) (end - 1)) + random.nextFloat();
	}
	
	public static float random(float start, float end) {
		return random.nextInt((int) (end - start - 1)) + random.nextFloat();
	}
}
