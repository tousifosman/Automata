package walker.statements;

import ast.StatementNode;
import java.io.*;
import java.util.Scanner;
import walker.ExpressionDelegate;
import walker.exceptions.StatementArgumentException;
import walker.exceptions.StatementExecutionException;

public class ReplaceStatement implements StatementExecutor {
    protected File directory;
    public ReplaceStatement(File directory) {
        this.directory = directory;
    }
    
    @Override
    public void execute(StatementNode node, ExpressionDelegate delegate) throws StatementExecutionException {
        if (!(node.value() instanceof String[])) {
            throw new StatementArgumentException(this.getClass().getSimpleName() + " Error: String[] required as argument");
        }

        String[] values = (String[]) node.value();

        if (values.length != 4) {
            throw new StatementArgumentException(this.getClass().getSimpleName() + " Error: Requires 4 arguments, " + values.length + " given");
        }


        String fileName1 = values[2];
        String fileName2 = values[3];

        if (fileName1.equals(fileName2)) {
            throw new StatementArgumentException(this.getClass().getSimpleName() + " Error: File1 and File2 cannot be the same file (" + fileName1 + ")");
        }

        File file1 = new File(directory, fileName1);
        File file2 = new File(directory, fileName2);

        if (!file1.exists())
            throw new StatementArgumentException(this.getClass().getSimpleName() + " Error: " + file1 + " doesn't exist");


        replace(values[0].replace(" ", ""), values[1], file1, file2);
    }

    protected void replace(String regex, String string, File fromFile, File toFile) throws StatementExecutionException {
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
                pwriter.println(newStr);
            }
            pwriter.flush();
        } catch (FileNotFoundException ex) {
            throw new StatementArgumentException(this.getClass().getSimpleName() + " Error: " + fromFile + " doesn't exist");
        } catch (IOException ex) {
            throw new StatementArgumentException(this.getClass().getSimpleName() + " Error: Cannot write to " + toFile);
        } finally {
            if (pwriter != null) {
                pwriter.close();
            }
        }
    }

    public static String type() {
        return "replace";
    }
}