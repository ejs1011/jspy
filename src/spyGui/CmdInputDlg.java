/*
 * Copyright 2015 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see licence.txt file for details.
 */

package spyGui;


import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;


public class CmdInputDlg extends JDialog {

    private static final long serialVersionUID = 1L;

    private CommandComboBox commandCombo = new CommandComboBox();
    private JButton btLaunch = new JButton("Run");
    private JLabel label1 = new JLabel("Cmd to launch", JLabel.LEFT);

    private static final File NULLFILE = new File("");
    private static Map<String, File> jnlpFiles = new HashMap<String, File>();

    public CmdInputDlg() {
        setName("execCmd");
        setTitle("Execute Command");
        setIconImage(new ImageIcon(getClass().getResource("spy.png")).getImage());

        Container contentPane = this.getContentPane();
        FlowLayout layout = new FlowLayout();
        contentPane.setLayout(layout);

        setSize(200, 300);

        setupComboBox();
        contentPane.add(label1);
        contentPane.add(commandCombo);
        contentPane.add(btLaunch);

        setupListeners();

        pack();
        setResizable(false);
    }

    private void setupListeners() {
        ActionListener btnAct = new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (arg0.getActionCommand().equals("Run")) {
                    executeCmd();
                }
            }
        };

        KeyListener kl = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    executeCmd();
                }
            }
        };

        btLaunch.addActionListener(btnAct);
        btLaunch.addKeyListener(kl);
        commandCombo.getEditor().getEditorComponent().addKeyListener(kl);
    }

    private void setupComboBox() {
        commandCombo.setEditable(true);
        commandCombo.setPreferredSize(new Dimension(200, 20));
    }

    public void executeCmd() {
        String cmdStr = ((JTextComponent) (commandCombo.getEditor().getEditorComponent())).getText();
        String origCmdStr = cmdStr;

        if (cmdStr != null && !cmdStr.trim().equals("")) {
            /*Check to see if using javaws.
            Then check to see if a URL is supplied instead of a local jnlp file
            Path to executable should not contain spaces*/
            if (cmdStr.toLowerCase().contains("javaws")) {
                if (cmdStr.toLowerCase().contains("http")) {
                    //Make sure there are no spaces in command string.  For example, use "C:\Progra~1" instead of C:\Program Files (x86)"
                    String commands[] = cmdStr.split(" ");
                    for (String command : commands) {
                        System.out.println(command);
                        if (command.contains("http")) {
                            //String tacos = getLocalCopy("http://eprnstg2.kroger.com:9081/TRexOneJWS/app/launch.jnlp?target=stg2");
                            String localJnlp = getLocalCopy(command);
                            System.out.println("Local jnlp file: " + localJnlp);
                            cmdStr += " " + localJnlp;
                        } else {
                            cmdStr = command;
                        }
                    }
                }
            }

            System.out.println("Executing command :" + cmdStr);
            List<String> arguments = new ArrayList<String>();
            arguments.addAll(Arrays.asList(cmdStr.trim().split("\\s+")));
            ProcessBuilder pb = new ProcessBuilder(arguments.toArray(new String[arguments.size()]));
            Map<String, String> env = pb.environment();

            URI jSpyJarUri = null;
            try {
                jSpyJarUri = spyAgent.AgentPreMain.class.getProtectionDomain()
                        .getCodeSource().getLocation().toURI();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            String jSpyJarPath = new File(jSpyJarUri).getPath();

            env.put("JAVA_TOOL_OPTIONS", "-javaagent:\"" + jSpyJarPath + "\"" + "=" + Integer.toString(SpyServer.serverPort));

            pb.redirectErrorStream(true);
            try {
                Process p = pb.start();
                Thread readProcTh = new Thread(new ProcessReader(p));
                readProcTh.start();
                commandCombo.addCommand(origCmdStr);
                setVisible(false);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Could not execute command", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //Added method to allow URL or jnlp as webstart argument.  Borrowed much of the code from Marathon project open-source repo.
    //https://github.com/jalian-systems/marathonv5/blob/master/marathon-java/marathon-java-driver/src/main/java/net/sourceforge/marathon/javadriver/JavaProfile.java
    private String getLocalCopy(String url) {
        File file = jnlpFiles.get(url);
        if (file == null) {
            file = createJNLPCopy(url);
            if (file != NULLFILE) {
                System.out.println("WebStart: Copied remote URL " + url + " to " + file.getAbsolutePath());
            } else {
                System.out.println("WebStart: Considering " + url + " as local");
            }
            jnlpFiles.put(url, file);
        }
        if (file == NULLFILE) {
            return url;
        }
        return file.getAbsolutePath();
    }

    private File createJNLPCopy(String urlSpec) {
        File jnlpFile = NULLFILE;
        OutputStream os = null;
        InputStream is = null;
        Object content = null;
        try {
            URL url = new URL(urlSpec);
            URLConnection openConnection = url.openConnection();
            content = openConnection.getContent();
            File tempFile = File.createTempFile("jspy", ".jnlp");
            tempFile.deleteOnExit();
            os = new FileOutputStream(tempFile);
            if (content instanceof InputStream) {
                is = (InputStream) content;
                byte[] b = new byte[1024];
                int n;
                while ((n = is.read(b)) != -1) {
                    os.write(b, 0, n);
                }
            }
            jnlpFile = tempFile;
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
            }
        }
        return jnlpFile;
    }
}