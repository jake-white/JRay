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
	boolean mouseLocked = false, mouseClicked = true;
	private final static Set<Integer> keyevents = new HashSet<Integer>();

	public boolean input(int key) {
		return keyevents.contains(key);

	}

	public void keyPressed(KeyEvent arg0) {
		Component thing = (Component) arg0.getSource();
		if(thing instanceof JFrame && arg0.getKeyCode()==KeyEvent.VK_ESCAPE){
			JFrame frame = (JFrame) thing;
			mouseLocked = false;
			Cursor cursor = Cursor.getDefaultCursor();
			frame.getContentPane().setCursor(cursor);
		}
		keyevents.add(arg0.getKeyCode());
	}

	public void keyReleased(KeyEvent arg0) {
		keyevents.remove(arg0.getKeyCode());
	}

	public void keyTyped(KeyEvent arg0) {

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		Component thing = (Component) arg0.getSource();
		if(thing instanceof JFrame && !mouseLocked){

			Robot bot;
			try {
				bot = new Robot();
				int sourceX = thing.getX()+thing.getWidth()/2;
				int sourceY = thing.getY()+thing.getHeight()/2;
				bot.mouseMove(sourceX, sourceY);
				System.out.println("click");
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
	
	public boolean getMouseClicked(){
		if(mouseClicked){
			mouseClicked = false;
			return true;
		}
		else return false;
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}
	
	public boolean isLocked(){
		return mouseLocked;
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
