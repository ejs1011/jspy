/*
 * Copyright 2015 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see licence.txt file for details.
 */

package spyGui;


import common.Utilities;

import javax.swing.*;
import java.awt.*;

public class AboutDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    public AboutDialog(JFrame parent) {
        setName("About");
        setTitle("About JSpy");
        setForeground(Color.BLACK);

        setIconImage(new ImageIcon(getClass().getResource("spy.png")).getImage());

        Container pane = this.getContentPane();
        setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));

        JLabel label1 = new JLabel("<html>"
                + "<center>JSpy for ROBOT Framework</center>"
                + "<center>Version 3.0.0</center>"
                + "</html>", JLabel.CENTER);
        JLabel label2 = new JLabel("Shows component details with an easy mouse move", JLabel.CENTER);
        JLabel label3 = new JLabel("<html>"
                + "<center>By Arulraj Samuel</center>"
                + "<center>Maintained by Robot Team in Nokia</center>"
                + "</html>", JLabel.CENTER);
        JLabel label4 = new JLabel("<html>"
                + "<center>Project was forked by Eric Schneider (ejs1011)</center>"
                + "</html>", JLabel.CENTER);

        label1.setForeground(Color.BLUE);
        label2.setForeground(Color.BLUE);
        label3.setForeground(Color.BLUE);
        label4.setForeground(Color.BLUE);

        JLabel dumm1 = new JLabel("-------------------------", JLabel.CENTER);
        JLabel dumm2 = new JLabel("-----------------------", JLabel.CENTER);
        JLabel dumm3 = new JLabel("-------------------", JLabel.CENTER);

        pane.add(label1);
        pane.add(dumm1);
        pane.add(label2);
        pane.add(dumm2);
        pane.add(label3);
        pane.add(dumm2);
        pane.add(label4);
        pane.add(dumm3);

        Utilities.centerContainerComponents(pane);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(parent);
        setAlwaysOnTop(true);
        setResizable(false);
        setVisible(true);

        this.validate();
    }

}
