/**
 * JToolBarButton.java
 *
 * @author Created by Omnicore CodeGuide
 */

package com.habibi.compiler.ui;

import javax.swing.*;
import java.awt.event.*;

public class JToolBarButton extends JButton
{
    public JToolBarButton() {
        super();
        setBorderPainted(false);
        setFocusPainted(false);
        addMouseListener(new JToolBarButton_MouseMotionListener());
    }

    public JToolBarButton(Action a) {
        super(a);
        setBorderPainted(false);
        setFocusPainted(false);
        addMouseListener(new JToolBarButton_MouseMotionListener());
    }

    public JToolBarButton(String text) {
        super(text);
        setBorderPainted(false);
        setFocusPainted(false);
        addMouseListener(new JToolBarButton_MouseMotionListener());
    }

    public JToolBarButton(Icon icon) {
        super(icon);
        setBorderPainted(false);
        setFocusPainted(false);
        addMouseListener(new JToolBarButton_MouseMotionListener());
    }

    public JToolBarButton(String text, Icon icon) {
        super(text, icon);
        setBorderPainted(false);
        setFocusPainted(false);
        addMouseListener(new JToolBarButton_MouseMotionListener());
    }

    private class JToolBarButton_MouseMotionListener extends MouseAdapter {

        public void mouseEntered(MouseEvent e) {
            setBorderPainted(true);
        }

        public void mouseExited(MouseEvent e) {
            setBorderPainted(false);
        }

    }
}

