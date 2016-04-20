package physics;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;

public class InputManager implements KeyListener, MouseListener{
	boolean mouseLocked = false, mouseClicked = false;
	private final static Set<Integer> keyevents = new HashSet<Integer>();

	public boolean input(int key) {
		return keyevents.contains(key);

	}

	public void keyPressed(KeyEvent thisEvent) {
		Component thing = (Component) thisEvent.getSource();
		if(thing instanceof JFrame && thisEvent.getKeyCode()==KeyEvent.VK_ESCAPE){
			JFrame frame = (JFrame) thing;
			mouseLocked = false;
			Cursor cursor = Cursor.getDefaultCursor();
			frame.getContentPane().setCursor(cursor);
		}
		keyevents.add(thisEvent.getKeyCode());
	}

	public void keyReleased(KeyEvent thisEvent) {
		keyevents.remove(thisEvent.getKeyCode());
	}
	
	public boolean getMouseClicked(){
		if(mouseClicked){
			mouseClicked = false;
			return true;
		}
		else return false;
	}

	
	public boolean isLocked(){
		return mouseLocked;
	}

	@Override
	public void mousePressed(MouseEvent thisEvent) {
		Component thing = (Component) thisEvent.getSource();
		if(thing instanceof JFrame && !mouseLocked){

			Robot bot;
			try {
				bot = new Robot();
				int sourceX = thing.getX()+thing.getWidth()/2;
				int sourceY = thing.getY()+thing.getHeight()/2;
				bot.mouseMove(sourceX, sourceY);
				mouseLocked = true;
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(thing instanceof JFrame){
			mouseClicked = true;
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent thisEvent) {
		Component thing = (Component) thisEvent.getSource();
		if(thing instanceof JFrame){
			mouseClicked = false;
		}
	}

	public void keyTyped(KeyEvent thisEvent) {
		//nothing to see here.
	}

	@Override
	public void mouseClicked(MouseEvent thisEvent) {
		//nothing to see here.
	}
	
	@Override
	public void mouseEntered(MouseEvent thisEvent) {
		//nothing to see here.
	}

	@Override
	public void mouseExited(MouseEvent thisEvent) {
		//nothing to see here.
	}
}
