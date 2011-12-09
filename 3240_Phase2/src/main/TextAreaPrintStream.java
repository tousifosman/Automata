package main;

import java.io.*;
import javax.swing.*;

public class TextAreaPrintStream implements PrintStream {
    private JTextArea textArea;
    private File logFile;

    public TextAreaPrintStream(JTextArea area) {
        this(area, null);
    }

    TextAreaPrintStream(JTextArea output, File file) {
        this.textArea = output;
        this.logFile = file;
    }

    @Override
    public void println(Object result) {
        textArea.append(result.toString() + "\n");

        if (logFile != null) {
            //Not so efficient, but w/e
            FileWriter f = null;
            BufferedWriter b = null;
            PrintWriter p = null;

            try {
                f = new FileWriter(logFile, true);
                b = new BufferedWriter(f);
                p = new PrintWriter(b);

                p.println(result.toString());
            } catch (IOException e) {
                System.out.println("Error logging output");
            } finally {
                if (p != null) p.close();
            }
        }
    }

    @Override
    public void print(Object result) {
        textArea.append(result.toString());

        if (logFile != null) {
            //Not so efficient, but w/e
            FileWriter f = null;
            BufferedWriter b = null;
            PrintWriter p = null;

            try {
                f = new FileWriter(logFile, true);
                b = new BufferedWriter(f);
                p = new PrintWriter(b);

                p.print(result.toString());
            } catch (IOException e) {
                System.out.println("Error logging output");
            } finally {
                if (p != null) p.close();
            }
        }
    }
}