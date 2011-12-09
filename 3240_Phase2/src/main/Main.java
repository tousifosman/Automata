package main;

import ast.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import walker.ASTWalker;
import walker.exceptions.ASTExecutionException;
import walker.expressions.*;
import walker.statements.*;

public class Main extends JPanel {
    private JButton loadButton, saveButton, runButton;
    private JCheckBox clearOutputOnRun, logOutput;
    private JTextArea code, output;
    private File directory;
    private JRootPane root;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Phase II");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new Main(frame.getRootPane()));

        frame.pack();
        frame.setVisible(true);
    }

    public Main(JRootPane root) {
        super(new BorderLayout());
        try {
            this.directory = (new File(".")).getCanonicalFile();
        } catch (IOException ex) {
            System.out.println("Error");
        }
        this.root = root;
        this.setPreferredSize(new Dimension(700, 700));

        loadButton = new JButton("Load");
        saveButton = new JButton("Save");
        saveButton.setEnabled(false);
        runButton = new JButton("Run");
        clearOutputOnRun = new JCheckBox("Clear Output Before Run", true);
        logOutput = new JCheckBox("Log Output", true);

        loadButton.addActionListener(new ButtonListener());
        runButton.addActionListener(new ButtonListener());
        saveButton.addActionListener(new ButtonListener());

        JPanel topBar = new JPanel();
        topBar.add(loadButton);
        topBar.add(saveButton);
        topBar.add(runButton);
        topBar.add(logOutput);
        topBar.add(clearOutputOnRun);

        code = new JTextArea(20, 70);
        JScrollPane codeScroll = new JScrollPane(code);
        code.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
        code.getDocument().addDocumentListener(new CodeListener());

        setupShortcuts();

        output = new JTextArea(10, 70);
        output.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(output);
        output.setFont(new Font(Font.MONOSPACED, Font.ITALIC, 15));

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, codeScroll, outputScroll);

        this.add(split, BorderLayout.CENTER);
        this.add(topBar, BorderLayout.NORTH);
    }

    private void run() {
        TextAreaPrintStream stream;

        if (logOutput.isSelected()) {
            stream = new TextAreaPrintStream(output, new File(directory, "output.txt"));
        } else {
            stream = new TextAreaPrintStream(output);
        }

        if (clearOutputOnRun.isSelected()) {
            output.setText("");
            if (logOutput.isSelected()) {
                File outputFile = new File(directory, "output.txt");
                if (outputFile.exists()) outputFile.delete();
            }
        }


        String input = code.getText();
        //"compile" code from 'code' text area, report areas, or create tree
        AbstractSyntaxTree tree = getTestTree();


        ASTWalker walker = new ASTWalker(stream, directory);
        try {
            walker.walk(tree);
        } catch (ASTExecutionException ex) {
            stream.println("!! " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Runtime Error!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadNewDocument() {
        JFileChooser chooser = new JFileChooser(directory);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        chooser.setFileFilter(new TXTFilter());

        chooser.showOpenDialog(this);
        directory = chooser.getCurrentDirectory();

        File chosenFile = chooser.getSelectedFile();
        try {
            loadFile(chosenFile);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error when reading input");
        }
    }

    private void loadFile(File chosenFile) throws FileNotFoundException {
        Scanner scan = new Scanner(chosenFile);
        StringBuilder completeText = new StringBuilder();
        if (scan.hasNextLine()) {
            completeText.append(scan.nextLine());
        }
        while (scan.hasNextLine()) {
            completeText.append("\n");
            String line = scan.nextLine();
            completeText.append(line);
        }

        code.setText(completeText.toString());
        root.putClientProperty("Window.documentModified", false);
        saveButton.setEnabled(false);
    }

    private void save() {
        root.putClientProperty("Window.documentModified", false);
        JFileChooser chooser = new JFileChooser(directory);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        chooser.setFileFilter(new TXTFilter());

        chooser.showSaveDialog(this);
        directory = chooser.getCurrentDirectory();

        File chosenFile = chooser.getSelectedFile();

        if (!chosenFile.getPath().toLowerCase().endsWith(".txt")) {
            chosenFile = new File(chosenFile.getPath() + ".txt");
        }


        FileWriter f = null;
        BufferedWriter b = null;
        PrintWriter p = null;

        try {
            f = new FileWriter(chosenFile, false);
            b = new BufferedWriter(f);
            p = new PrintWriter(b);

            p.print(code.getText());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving to " + chosenFile.getName(), "Error saving MinRE", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (p != null) p.close();
        }



        saveButton.setEnabled(false);
    }

    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == loadButton) {
                loadNewDocument();
            } else if (ae.getSource() == runButton) {
                run();
            } else if (ae.getSource() == saveButton) {
                save();
            }
        }
    }

    private void setupShortcuts() {
        InputMap inputMap = code.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = code.getActionMap();
        KeyStroke stroke = KeyStroke.getKeyStroke("ctrl S");
        Action action = new AbstractAction("save") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (saveButton.isEnabled()) save();
            }
        };
        inputMap.put(stroke, "save");
        stroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.META_DOWN_MASK);
        inputMap.put(stroke, "save");
        actionMap.put("save", action);

        stroke = KeyStroke.getKeyStroke("ctrl L");
        action = new AbstractAction("load") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                loadNewDocument();
            }
        };
        inputMap.put(stroke, "load");
        stroke = KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.META_DOWN_MASK);
        inputMap.put(stroke, "load");
        actionMap.put("load", action);

        stroke = KeyStroke.getKeyStroke("ctrl R");
        action = new AbstractAction("run") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                run();
            }
        };
        inputMap.put(stroke, "run");
        stroke = KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.META_DOWN_MASK);
        inputMap.put(stroke, "run");
        actionMap.put("run", action);
    }

    private class CodeListener implements DocumentListener {
        private void setUpdated() {
            root.putClientProperty("Window.documentModified", true);
            saveButton.setEnabled(true);
        }

        public void insertUpdate(DocumentEvent de) {
            setUpdated();
        }

        @Override
        public void removeUpdate(DocumentEvent de) {
            setUpdated();
        }

        @Override
        public void changedUpdate(DocumentEvent de) {
            setUpdated();
        }
    }

    private AbstractSyntaxTree getTestTree() {
        StatementNode head = new StatementNode(AssignStatement.type(), "match_the");
        ExpressionNode binop = new ExpressionNode(BinopExpression.type(), null);
        ExpressionNode term = new ExpressionNode(TermExpression.type(), new String[]{"[A-Z a-z]*the[A-Z a-z]*", "input.txt"});
        binop.addSubnode(term);
        head.addSubnode(binop);

        StatementNode second = new StatementNode(AssignStatement.type(), "match_th");
        ExpressionNode binop2 = new ExpressionNode(BinopExpression.type(), null);
        ExpressionNode term2 = new ExpressionNode(TermExpression.type(), new String[]{"th[A-Z a-z]*", "input.txt"});
        binop2.addSubnode(term2);
        second.addSubnode(binop2);

        head.setNextNode(second);

        StatementNode third = new StatementNode(AssignStatement.type(), "count");
        ExpressionNode the_size = new ExpressionNode(SizeExpression.type(), null);
        ExpressionNode the_list = new ExpressionNode(IdExpression.type(), "match_the");
        third.addSubnode(the_size);
        the_size.addSubnode(the_list);

        second.setNextNode(third);

        StatementNode fourth = new StatementNode(PrintStatement.type(), null);
        ExpressionNode count = new ExpressionNode(IdExpression.type(), "count");
        fourth.addSubnode(count);

        third.setNextNode(fourth);

        StatementNode fifth = new StatementNode(AssignStatement.type(), "count");
        ExpressionNode th_size = new ExpressionNode(SizeExpression.type(), null);
        ExpressionNode th_list = new ExpressionNode(IdExpression.type(), "match_th");
        fifth.addSubnode(th_size);
        th_size.addSubnode(th_list);

        fourth.setNextNode(fifth);

        StatementNode sixth = new StatementNode(PrintStatement.type(), null);
        sixth.addSubnode(count);
        sixth.addSubnode(th_list);

        fifth.setNextNode(sixth);

        StatementNode seventh = new StatementNode(PrintStatement.type(), null);
        ExpressionNode binop3 = new ExpressionNode(BinopExpression.type(), "union");
        ExpressionNode term3 = new ExpressionNode(TermExpression.type(), new String[]{"[A-Z a-z]*z[A-Z a-z]*", "input.txt"});
        ExpressionNode binop4 = new ExpressionNode(BinopExpression.type(), null);
        ExpressionNode term4 = new ExpressionNode(TermExpression.type(), new String[]{"[A-Z a-z]*q[A-Z a-z]*", "input.txt"});
        seventh.addSubnode(binop3);
        binop3.addSubnode(term3);
        binop3.addSubnode(binop4);
        binop4.addSubnode(term4);

        sixth.setNextNode(seventh);

        StatementNode eighth = new StatementNode(ReplaceStatement.type(), new String[]{"we|We|they|They", "I", "input.txt", "result1.txt"});
        seventh.setNextNode(eighth);

        StatementNode ninth = new StatementNode(ReplaceStatement.type(), new String[]{"h[A-z a-z]*", "anana", "input.txt", "result2.txt"});
        eighth.setNextNode(ninth);

        return new AbstractSyntaxTree(head);
    }
}
