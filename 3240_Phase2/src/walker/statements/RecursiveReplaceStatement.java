package walker.statements;

import java.io.File;
import walker.exceptions.StatementExecutionException;

public class RecursiveReplaceStatement extends ReplaceStatement {
    @Override
    protected boolean replace(String regex, String string, File fromFile, File toFile) throws StatementExecutionException {
        if (regex.equals(string)) {
            // Taylor TODO - better error
            throw new StatementExecutionException("Recursive Replace: Regex cannot equal string");
        }
        File tempFile_0 = new File(toFile.getParent(), toFile.getName() + "_tmp0");
        File tempFile_1 = new File(toFile.getParent(), toFile.getName() + "_tmp1");

        boolean changed = super.replace(regex, string, fromFile, tempFile_0);
        boolean changedAtAll = changed;
        boolean fileSwitch = true;

        while (changed) {
            if (fileSwitch) {
                changed = super.replace(regex, string, tempFile_0, tempFile_1);
            } else {
                changed = super.replace(regex, string, tempFile_1, tempFile_0);
            }
            fileSwitch = !fileSwitch;
        }
        
        if(fileSwitch) {
            tempFile_0.renameTo(toFile);
            tempFile_1.deleteOnExit();
        } else {
            tempFile_1.renameTo(toFile);
            tempFile_0.deleteOnExit();
        }

        return changedAtAll;
    }
    
    public static String type() {
        return "recursivereplace REGEX with ASCII-STR in  <file-names> ;";
    }
}
