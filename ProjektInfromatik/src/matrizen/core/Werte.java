package matrizen.core;

import java.util.HashMap;

public class Werte {
	private HashMap<String, Integer> map;

	public Werte() {
		map = new HashMap<>();
	}

	public Werte put(String s, Integer i) {
		map.put(s.toLowerCase(), i);
		return this;
	}

	public int get(String s) {
		return map.get(s.toLowerCase());
	}

}
