package walker.statements;

import ast.StatementNode;
import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import walker.ExpressionDelegate;
import walker.exceptions.StatementExecutionException;

public class ReplaceStatement implements StatementExecutor {
    @Override
    public void execute(StatementNode node, ExpressionDelegate delegate) throws StatementExecutionException {
        if (!(node.value() instanceof String[])) {
            // Taylor TODO - Better error
            throw new StatementExecutionException("Incorrect values for replace statement");
        }

        String[] values = (String[]) node.value();

        if (values.length != 4) {
            // Taylor TODO - Better error
            throw new StatementExecutionException("Replace statement requires 4 arguments");
        }


        String fileName1 = values[2];
        String fileName2 = values[3];

        if (fileName1.contains(fileName2)) {
            // Taylor TODO - Better error
            throw new StatementExecutionException("File1 and File2 cannot be the same file");
        }

        File file1 = new File(fileName1);
        File file2 = new File(fileName2);

        if (!file1.exists())
            throw new StatementExecutionException("File1 doesn't exist"); //Taylor TODO - better


        replace(values[0].replace(" ", ""), values[1], file1, file2);
    }

    protected boolean replace(String regex, String string, File fromFile, File toFile) throws StatementExecutionException {
        FileWriter fwriter = null;
        BufferedWriter bwriter = null;
        PrintWriter pwriter = null;
        boolean changed = false;
        try {
            fwriter = new FileWriter(toFile);
            bwriter = new BufferedWriter(fwriter);
            pwriter = new PrintWriter(bwriter);
            Scanner scan = new Scanner(fromFile);
            while (scan.hasNextLine()) {
                String str = scan.nextLine();
                String newStr = str.replaceAll(regex, string);
                if (!str.equals(newStr)) changed = true;
                pwriter.println(newStr);
                System.out.println(str + ":" + newStr);
            }
            pwriter.flush();
        } catch (FileNotFoundException ex) {
            throw new StatementExecutionException("File1 doesn't exist"); //Taylor TODO - better
        } catch (IOException ex) {
            throw new StatementExecutionException("Unable to write to file2"); //Taylor TODO - better
        } finally {
            if (pwriter != null) {
                pwriter.close();
            }
            return changed;
        }
    }

    public static String type() {
        return "replace REGEX with ASCII-STR in  <file-names> ;";
    }
}