package matrizen.model;

public abstract class Zauberstab {
	protected int schaden, delay, reichweite, geschw;

	public abstract void schuss();

	public int getDelay() {
		return delay;
	}
}
