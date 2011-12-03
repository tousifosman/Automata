package walker.expressions;

import ast.ExpressionNode;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import walker.ExpressionDelegate;
import walker.exceptions.ExpressionExpansionException;

public class TermExpression implements ExpressionExpander {
    @Override
    public List expand(ExpressionNode node, ExpressionDelegate delegate) throws ExpressionExpansionException {
        if (!(node.value() instanceof String[])) {
            // Taylor TODO - Better exception
            throw new ExpressionExpansionException("Value must be array");
        }
        String[] values = (String[]) node.value();

        if (values.length != 2) {
            // Taylor TODO - Better exception
            throw new ExpressionExpansionException("TermExpressions require two arguments");
        }

        String regex = values[0];
        String fileName = values[1];

        File file = new File(fileName);

        if (!file.exists())
            throw new ExpressionExpansionException("File doesn't exist"); //Taylor TODO - better

        return listFromFile(file, regex);
    }

    private List<String> listFromFile(File file, String regex) throws ExpressionExpansionException {
        try {
            Scanner scan = new Scanner(file);
            
            List<String> wordList = new LinkedList<String>();
            while(scan.hasNext()) {
                String input = scan.next();
                input.matches(regex);
            }
            return wordList;

        } catch (FileNotFoundException ex) {
            throw new ExpressionExpansionException("File doesn't exist"); //Taylor TODO - better
        }
    }

    public static String type() {
        return "<term>";
    }
}
