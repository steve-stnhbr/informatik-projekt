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
		if (!map.containsKey(s))
			throw new WertNichtGefundenException(s);
		return map.get(s.toLowerCase());
	}

	public HashMap<String, Integer> getMap() {
		return map;
	}

	public Werte setMap(HashMap<String, Integer> map) {
		this.map = map;
		return this;
	}

}
