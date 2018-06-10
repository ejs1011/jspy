package common;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
//import java.nio.file.Files;
//import java.nio.file.Paths;
import java.util.ArrayList;

public class Utilities {
    public static void centerContainerComponents(Container c) {
        for (Component component : c.getComponents()) {
            JComponent jComponent = (JComponent) component;
            jComponent.setAlignmentX(Component.CENTER_ALIGNMENT);
        }
    }

    public static void copyStringToClipboard(String str) {
        StringSelection selection = new StringSelection(str);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, null);
    }

    public static ArrayList<String> getCommandHistory() {
        ArrayList<String> commands = new ArrayList<String>();

/*
        if (!Files.exists(Paths.get(Constants.COMMAND_HISTORY_FILENAME))) {
            return commands;
        }
*/
        //java 6 changes
        //File f = new File(".command_history_jspy.txt");


        File f = new File(Constants.COMMAND_HISTORY_FILENAME);
        System.out.println(f.getAbsolutePath());
        if (!f.exists()) {
            System.out.println("file exists");
            return commands;
        }

/*        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(Constants.COMMAND_HISTORY_FILENAME))) {
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                commands.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        //removed try with resources for java 6 compatibility
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(Constants.COMMAND_HISTORY_FILENAME));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                commands.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return commands;
    }

    /*    public static void writeCommandHistory(Object[] cmd) {
            try (PrintStream out = new PrintStream(Constants.COMMAND_HISTORY_FILENAME)) {
                for (Object com : cmd) {
                    out.println(com);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

    //removed try with resources for java 6 compatibility
    public static void writeCommandHistory(Object[] cmd) {
        PrintStream out = null;
        try {
            out = new PrintStream(Constants.COMMAND_HISTORY_FILENAME);
            for (Object com : cmd) {
                out.println(com);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out !=null){
                out.close();
            }
        }
    }
}
