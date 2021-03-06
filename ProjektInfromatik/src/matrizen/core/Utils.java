package matrizen.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.xml.bind.ValidationException;

import matrizen.model.Spiel;

public class Utils {
	private static final Random random = new Random();

	public static int random(int end) {
		return random.nextInt(end);
	}

	public static int random(int start, int end) {
		return random.nextInt(end - start) + start;
	}

	public static int random(int start, int end, int... ex) {
		int r = 0;

		do {
			r = random(start, end);
		} while (Arrays.asList(ex).contains(r));

		return r;
	}

	@SuppressWarnings("unchecked")
	public static <E> List<E> listeUmformen(List<?> l) {
		List<E> list = new ArrayList<E>();
		for (Object o : l)
			list.add((E) o);

		return list;
	}

	public static <E> String arrayToString(E[] a) {
		validate(a);
		StringBuilder builder = new StringBuilder();
		builder.append(a[0].getClass().getName() + ": [");
		for (int i = 0; i < a.length - 1; i++) {
			builder.append(a[i].toString());
			builder.append(", ");
		}

		builder.append(a[a.length - 1].toString());

		builder.append("]");
		return builder.toString();
	}

	public static String arrayToString(boolean[] a) {
		if (validate(a)) {
			StringBuilder builder = new StringBuilder();
			builder.append("boolean: [");
			for (int i = 0; i < a.length - 1; i++) {
				builder.append(a[i]);
				builder.append(", ");
			}

			builder.append(a[a.length - 1]);

			builder.append("]");
			return builder.toString();
		}

		return null;
	}

	private static boolean validate(Object o) {
		if (o == null)
			try {
				throw new ValidationException("Object " + o + " is null");
			} catch (ValidationException e) {
				e.printStackTrace();
				return false;
			}
		else
			return true;
	}

	public static <E> List<E> arrayToList(E[] e) {
		List<E> l = new ArrayList<E>();

		for (E i : e)
			l.add(i);

		return l;
	}

	public static boolean vektorIstInFeld(Vektor p) {
		if (p.mag() > 100)
			p.div(32).round();
		return p.getX() > 0 && p.getY() > 0 && p.getX() < Spiel.spalten * Spiel.feldLaenge
				&& p.getY() < Spiel.zeilen * Spiel.feldLaenge;
	}

	public static float begrenzen(float dist, int i, int j) {
		if(dist > j)
			return j;
		else
			return dist;
	}
}
