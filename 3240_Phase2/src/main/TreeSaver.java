package main;

import ast.*;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class TreeSaver {
    public static void save(AbstractSyntaxTree tree, File file) {
        FileWriter f = null;
        BufferedWriter b = null;
        PrintWriter p = null;

        try {
            f = new FileWriter(file, false);
            b = new BufferedWriter(f);
            p = new PrintWriter(b);

            p.append(tree.toString());
        } catch (IOException e) {
            System.out.println("Error logging output");
        } finally {
            if (p != null) p.close();
        }
    }

    public static AbstractSyntaxTree load(File file) {
        try {
            return load(new Scanner(file));
        } catch (FileNotFoundException ex) {
            System.out.println("file not found");
            return null;
        }
    }

    public static AbstractSyntaxTree load(String string) {
        return load(new Scanner(string));
    }

    private static AbstractSyntaxTree load(Scanner scan) {
        AbstractSyntaxTree tree = new AbstractSyntaxTree();

        if (scan.hasNextLine()) {
            String line = scan.nextLine();
            Scanner lineScan = new Scanner(line);
            String nodeClass = lineScan.next();
            String type = lineScan.next();
            Object value = getValue(lineScan.nextLine());

            Node prevNode;

            if (nodeClass.equals("StatementNode")) {
                prevNode = new StatementNode(type, value);
            } else {
                prevNode = new ExpressionNode(type, value);
            }

            tree.setHead(prevNode);

            int prevScope = 0;
            while (scan.hasNextLine()) {
                int currScope = 0;

                line = scan.nextLine();
                while (line.startsWith("\t")) {
                    currScope += 1;
                    line = line.replaceFirst("\t", "");
                }

                lineScan = new Scanner(line);
                nodeClass = lineScan.next();
                type = lineScan.next();
                value = getValue(lineScan.nextLine());

                Node currNode;

                if (nodeClass.equals("StatementNode")) {
                    currNode = new StatementNode(type, value);
                } else {
                    currNode = new ExpressionNode(type, value);
                }

                if (currScope == prevScope) {
                    if (currScope == 0) {
                        prevNode.setNextNode(currNode);
                    } else {
                        Node parent = prevNode.getParent();
                        parent.addSubnode(currNode);
                    }
                } else if (currScope == prevScope + 1) {
                    prevNode.addSubnode(currNode);
                } else if (currScope > prevScope) {
                    System.out.println("error");
                } else {
                    int tempScope = prevScope;
                    while (tempScope > currScope) {
                        prevNode = prevNode.getParent();
                        tempScope--;
                    }

                    if (tempScope == 0) {
                        prevNode.setNextNode(currNode);
                    } else {
                        Node parent = prevNode.getParent();
                        parent.addSubnode(currNode);
                    }
                }
                prevScope = currScope;
                prevNode = currNode;
            }
        }
        return tree;
    }

    private static Object getValue(String nextLine) {
        nextLine = nextLine.trim();
        if(nextLine.startsWith("[") && nextLine.endsWith("]")) {
            nextLine = nextLine.substring(1, nextLine.length()-1);
            return nextLine.split(", ");
        } else { 
            return nextLine;
        }
    }
}
