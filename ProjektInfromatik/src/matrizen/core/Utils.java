package matrizen.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {
	private static final Random random = new Random();
	
	public static float random(float end) {
		return random.nextInt((int) (end - 1)) + random.nextFloat();
	}
	
	public static float random(float start, float end) {
		return random.nextInt((int) (end - start - 1)) + random.nextFloat();
	}
	
	public static <E> List<E> listeUmformen(List<?> l) {
		List<E> list = new ArrayList<E>();
		for(Object o : l)
			list.add((E) o);
		
		return list;
	}
}
