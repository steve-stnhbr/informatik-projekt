package matrizen.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {
	private static final Random random = new Random();
	
	public static int random(int end) {
		return random.nextInt(end);
	}
	
	public static int random(int start, int end) {
		return random.nextInt(end - start) + start;
	}
	
	@SuppressWarnings("unchecked")
	public static <E> List<E> listeUmformen(List<?> l) {
		List<E> list = new ArrayList<E>();
		for(Object o : l)
			list.add((E) o);
		
		return list;
	}
}
