package matrizen.core;

import static matrizen.view.SpielFenster.logger;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;

import java.util.Timer;
import java.util.TimerTask;

public class MusikPlayer {
	static private AudioInputStream momentanesLied;
	static private Vector<AudioInputStream> schlange;
	static private Clip clip;
	static private Timer timer;
	static private boolean weiter = true, wiederholen, zufall;
	static private float volume = .5f;

	private MusikPlayer() {
	}

	static public void setVolume(int i) {
		setVolume(i / 100);
	}

	static public void setVolume(float i) {
		if (i >= 0 && i <= 1) {
			volume = i;
			if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
				FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				float range = gainControl.getMaximum() - gainControl.getMinimum();
				float gain = (range * i) + gainControl.getMinimum();
				gainControl.setValue(gain);
			}
		}
	}

	static public float getVolume() {
		return volume;
	}

	static public void laden(AudioInputStream... s) {
		for (AudioInputStream a : s)
			schlange.add(a);

		if (zufall)
			Collections.shuffle((List<?>) schlange);
	}

	static public void naechsterTitel(AudioInputStream s) {
		AudioInputStream[] a = new AudioInputStream[schlange.size()];
		a = schlange.toArray(a);
		List<AudioInputStream> l = Utils.<AudioInputStream>arrayToList(a);
		Collections.reverse(l);
		l.add(s);
		Collections.reverse(l);
		schlange = new Vector<>(l);
	}

	static public void abspielen() {
		try {
			logger.log(Level.INFO, "nächstes Lied wird abgespielt");
			clip.open(momentanesLied);
			setVolume(volume);
			clip.start();
			AudioFormat format = momentanesLied.getFormat();
			long frames = momentanesLied.getFrameLength();
			setTimer((int) (frames / format.getFrameRate()) * 1000);
		} catch (LineUnavailableException | IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	static public void stop() {
		clip.stop();
		timer.cancel();
	}

	static private void setTimer(long frameLength) {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				if (wiederholen)
					schlange.add(momentanesLied);
				anhalten();
				naechstesLied();
				if (weiter)
					abspielen();
				this.cancel();
			}

		}, (int) frameLength, 1);
	}

	static public void naechstesLied() {
		momentanesLied = schlange.remove(0);
	}

	static private void anhalten() {
		momentanesLied = null;
		clip.stop();
		clip.flush();
		clip.close();
	}

	public static boolean isWeiter() {
		return weiter;
	}

	public static void setWeiter(boolean weiter) {
		MusikPlayer.weiter = weiter;
	}

	public static boolean isWiederholen() {
		return wiederholen;
	}

	public static void setWiederholen(boolean wiederholen) {
		MusikPlayer.wiederholen = wiederholen;
	}

	public static boolean isZufall() {
		return zufall;
	}

	public static void setZufall(boolean zufall) {
		MusikPlayer.zufall = zufall;
	}

	static {
		if (schlange == null)
			schlange = new Vector<AudioInputStream>();
		if (clip == null) {
			try {
				clip = AudioSystem.getClip();
			} catch (Exception e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}
}
