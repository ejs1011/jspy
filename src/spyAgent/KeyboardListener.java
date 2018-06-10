/*
 * Copyright 2015 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see licence.txt file for details.
 */

package spyAgent;

import common.Utilities;

import java.awt.*;
import java.awt.event.KeyEvent;

public class KeyboardListener implements KeyEventDispatcher {
    public static String highlightedComponentName = "";
    //TODO added this
    public static String highlightedWindowName = "";

    private WindowTracker winTrack;
    private boolean shiftPressed = false, ctrlPressed = false;

    public KeyboardListener(WindowTracker winTrack) {
        this.winTrack = winTrack;
    }

/*    public boolean dispatchKeyEvent(KeyEvent arg0) {
        if (arg0.getID() == KeyEvent.KEY_PRESSED) {
            if (arg0.getKeyCode() == KeyEvent.VK_ALT) {
                shiftPressed = true;
            } else if (arg0.getKeyCode() == KeyEvent.VK_CONTROL) {
                ctrlPressed = true;
            } else if (shiftPressed && ctrlPressed && arg0.getKeyCode() == KeyEvent.VK_R) {
                Communicator.writeToServer("Pressed ctrl+alt+R");
                System.out.println("Re-Indexing the Components");
                Thread enuTh = new Thread(new CompEnum(winTrack.activeWindow));
                enuTh.start();
            } else if (ctrlPressed && shiftPressed && arg0.getKeyCode() == KeyEvent.VK_C) {
                Utilities.copyStringToClipboard(highlightedComponentName);
            } else if (ctrlPressed && shiftPressed && arg0.getKeyCode() == KeyEvent.VK_S) {
                CompMouseListner.setActive = !CompMouseListner.setActive;
            }

        } else if (arg0.getID() == KeyEvent.KEY_RELEASED) {
            if (arg0.getKeyCode() == KeyEvent.VK_ALT) {
                shiftPressed = false;
            } else if (arg0.getKeyCode() == KeyEvent.VK_CONTROL) {
                ctrlPressed = false;
            }
        }
        return false;
    }*/

    //Modified this method to use the F11 and F12 keys in place of  Ctrl-Alt-R because
    //  those keys activated menus in swing application being spied
    public boolean dispatchKeyEvent(KeyEvent arg0) {
        if (arg0.getID() == KeyEvent.KEY_PRESSED) {
            if (arg0.getKeyCode() == KeyEvent.VK_SHIFT) {
                shiftPressed = true;
            } else if (shiftPressed && arg0.getKeyCode() == KeyEvent.VK_F11) {
                Utilities.copyStringToClipboard(highlightedWindowName);
                Communicator.writeToServer("Copied Window: " + highlightedWindowName);
                System.out.println("Window Name: " + highlightedWindowName);
            } else if (shiftPressed && arg0.getKeyCode() == KeyEvent.VK_F12) {
                CompMouseListner.setActive = !CompMouseListner.setActive;
                Communicator.writeToServer("Press Shift+F12 to resume inspection");
                System.out.println("Stopped inspection.");
            } else if (arg0.getKeyCode() == KeyEvent.VK_F12) {
                Communicator.writeToServer("Pressed F12");
                System.out.println("Re-Indexing the Components");
                Thread enuTh = new Thread(new spyAgent.CompEnum((Component) winTrack.activeWindow));
                enuTh.start();
            } else if (arg0.getKeyCode() == KeyEvent.VK_F11) {
                Utilities.copyStringToClipboard(highlightedComponentName);
                Communicator.writeToServer("Copied Name: " + highlightedComponentName);
                System.out.println("Component Name: " + highlightedComponentName);
            }
        } else if (arg0.getID() == KeyEvent.KEY_RELEASED) {
            if (arg0.getKeyCode() == KeyEvent.VK_SHIFT) {
                shiftPressed = false;
            }
        }
        return false;
    }
}