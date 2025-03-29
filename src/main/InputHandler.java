package main;

import java.awt.event.*;

public class InputHandler implements KeyListener {
    public static boolean up, down, left, right, shoot;

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W: 
                up = true;
                break;
            case KeyEvent.VK_S: 
                down = true;
                break;
            case KeyEvent.VK_A: 
                left = true;
                break;
                case KeyEvent.VK_D: 
                    right = true;
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    up = false;
                    break;
                case KeyEvent.VK_S:
                    down = false;
                    break;
                case KeyEvent.VK_A:
                    left = false;
                    break;
                case KeyEvent.VK_D:
                    right = false;
                    break;
                case KeyEvent.VK_SPACE:
                    shoot = false;
                    break;
            }
        }
        

    @Override
    public void keyTyped(KeyEvent e) {}
}