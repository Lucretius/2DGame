package com.lippens.wiggame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener{

	public boolean released = false;

	public int pressDir = 0;
	public InputHandler(Game game) {
		game.addKeyListener(this);
	}
	
	public class Key {
		private int numTimesPressed = 0;
		public boolean pressed = false;
		public boolean released = false;
		public boolean lock;
		
		public int getNumTimesPressed() {
			return numTimesPressed;
		}
		
		public boolean isPressed() {
			return pressed;
		}
		
		public void toggle(boolean isPressed) {
			pressed = isPressed;
			if (isPressed) numTimesPressed++;
		}
		
		public boolean isReleased() {

			return this.released;
		}
		
		public void toggleRel(boolean isReleased) {
			this.released = isReleased;
			if (isReleased) {}
		}
	}

	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();
	public Key space = new Key();
	public Key one = new Key();
	public Key two = new Key();
	public Key f = new Key();
	
	public void keyTyped(KeyEvent e) {
		
		
	}

	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode(), true);
		
	}

	public void keyReleased(KeyEvent e) {
		toggleKey(e.getKeyCode(), false);
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_SPACE) {
			space.released = true;
		}
		if (key == KeyEvent.VK_W) {
			up.lock = false;
		}
		if (key == KeyEvent.VK_S) {
			down.lock = false;
		}
		if (key == KeyEvent.VK_A) {
			left.lock = false;
		}
		if (key == KeyEvent.VK_D) {
			right.lock = false;
		}
		if (key == KeyEvent.VK_F) {
			f.released = true;

		}
	}
	
	public void toggleKey(int keyCode, boolean isPressed) {
		if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
			up.lock = true;
			if (left.lock == true && right.lock == false) {
				pressDir = 2;}
			else if (left.lock == false && right.lock == true) {
				pressDir = 3;
			}
			up.toggle(isPressed); 
		}
		if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
			down.lock = true;
			if (left.lock == true && right.lock == false) {
				pressDir = 2;}
			else if (left.lock == false && right.lock == true) {
				pressDir = 3;
			}
			down.toggle(isPressed);
			}
		if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
			left.lock =true;
			if (up.lock == true && down.lock == false) {
				pressDir = 0;}
			else if (down.lock == true && up.lock == false) {
				pressDir = 1;
			}
			left.toggle(isPressed);

			}
		if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
			right.lock = true;
			if (up.lock == true && down.lock == false) {
				pressDir = 0;}
			else if (down.lock == true && up.lock == false) {
				pressDir = 1;
			}
			right.toggle(isPressed); 
			}
		if (keyCode == KeyEvent.VK_SPACE) {
			space.toggle(isPressed);

			}

		if (keyCode == KeyEvent.VK_1) {
			one.toggleRel(isPressed);
			one.toggle(isPressed);
			}
		if (keyCode == KeyEvent.VK_2) {
			two.toggleRel(isPressed);
			two.toggle(isPressed);
			}
		

		if (keyCode == KeyEvent.VK_F) {
			f.toggle(isPressed);
		}
	}

}
