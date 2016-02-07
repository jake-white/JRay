package physics;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class InputManager implements KeyListener {
	private final static Set<Integer> keyevents = new HashSet<Integer>();


	public boolean input(int key) {
		return keyevents.contains(key);

	}

	public void keyPressed(KeyEvent arg0) {
		keyevents.add(arg0.getKeyCode());
	}

	public void keyReleased(KeyEvent arg0) {
		keyevents.remove(arg0.getKeyCode());
	}

	public void keyTyped(KeyEvent arg0) {

	}
}
