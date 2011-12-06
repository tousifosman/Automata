package walker.statements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import walker.exceptions.StatementArgumentException;
import walker.exceptions.StatementExecutionException;

public class RecursiveReplaceStatement extends ReplaceStatement {
    @Override
    protected void replace(String regex, String string, File fromFile, File toFile) throws StatementExecutionException {
        if (regex.equals(string)) {
            throw new StatementArgumentException("RecursiveReplaceStatement: Regex cannot equal string (" + regex + ")");
        }

        FileWriter fwriter = null;
        BufferedWriter bwriter = null;
        PrintWriter pwriter = null;
        try {
            fwriter = new FileWriter(toFile);
            bwriter = new BufferedWriter(fwriter);
            pwriter = new PrintWriter(bwriter);
            Scanner scan = new Scanner(fromFile);
            while (scan.hasNextLine()) {
                String str = scan.nextLine();
                String newStr = str.replaceAll(regex, string);
                String currStr = newStr;
                boolean changed = !str.equals(newStr);
                while(changed) {
                    newStr = currStr.replaceAll(regex, string);
                    changed = !currStr.equals(newStr);
                    currStr = newStr;
                }
                pwriter.println(newStr);
            }
            pwriter.flush();
        } catch (FileNotFoundException ex) {
            throw new StatementArgumentException("ReplaceStatement Error: " + fromFile + " doesn't exist");
        } catch (IOException ex) {
            throw new StatementArgumentException("ReplaceStatement Error: Cannot write to " + toFile);
        } finally {
            if (pwriter != null) {
                pwriter.close();
            }
        }
    }

    public static String type() {
        return "recursivereplace REGEX with ASCII-STR in  <file-names> ;";
    }
}
