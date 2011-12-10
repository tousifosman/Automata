package walker.expressions;

import ast.ExpressionNode;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import walker.ExpressionDelegate;
import walker.datastructs.StringList;
import walker.datastructs.StringWithMetaData;
import walker.exceptions.ExpressionArgumentException;
import walker.exceptions.ExpressionExpansionException;

public class TermExpression implements ExpressionExpander {
    private File directory;
    
    public TermExpression(File directory) {
        this.directory = directory;
    }
    @Override
    public List expand(ExpressionNode node, ExpressionDelegate delegate) throws ExpressionExpansionException {
        if (!(node.value() instanceof String[])) {
            throw new ExpressionArgumentException(this.getClass().getSimpleName() + " Error: Value must be String[]");
        }
        String[] values = (String[]) node.value();

        if (values.length != 2) {
            throw new ExpressionArgumentException(this.getClass().getSimpleName() + " Error: Must have two arguments (" + values.length + " given)");
        }

        String regex = values[0].replace(" ", "");
        String fileName = values[1];

        File file = new File(directory, fileName);

        if (!file.exists())
            throw new ExpressionArgumentException(this.getClass().getSimpleName() + " Error: " + file + " doesn't exist");

        return listFromFile(file, regex);
    }

    private List<StringWithMetaData> listFromFile(File file, String regex) throws ExpressionExpansionException {
        try {
            Scanner scan = new Scanner(file);

            List<StringWithMetaData> wordList = new StringList();
            int lineNumber = 0;

            while (scan.hasNextLine()) {
                String line = scan.nextLine();

                Scanner lineScanner = new Scanner(line);
                while (lineScanner.hasNext()) {
                    String input = lineScanner.next();
                    if (input.matches(regex)) {
                        int startIndex = line.indexOf(input);
                        int endIndex = startIndex + input.length();
                        wordList.add(new StringWithMetaData(input, file.getName(), lineNumber, startIndex, endIndex));
                    }
                }

                lineNumber++;
            }
            return wordList;

        } catch (FileNotFoundException ex) {
            throw new ExpressionArgumentException(this.getClass().getSimpleName() + " Error: " + file + " doesn't exist");
        }
    }

    public static String type() {
        return "term";
    }
}
