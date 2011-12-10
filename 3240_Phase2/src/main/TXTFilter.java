/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author taylor
 */
class TXTFilter extends FileFilter {

    @Override
    public boolean accept(File file) {
        return file.getName().endsWith(".txt") || file.getName().endsWith(".c");
    }

    @Override
    public String getDescription() {
        return "miniRE file (.txt) and compiled ASTs (.c)";
    }
    
}
