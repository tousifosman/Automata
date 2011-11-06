
import automata.Token;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * Generates and stores XML-like structures to represent the tokens that are 
 * read and parsed.
 */
public class XMLBuilder {
    private LinkedList xml;
    private List<Token> started;
    public List<List<String>> savedXML;

    public XMLBuilder() {
        xml = new LinkedList();
        started = new LinkedList<Token>();
        savedXML = new LinkedList<List<String>>();
    }

    /**
     * Adds the given tokens and character to the current xml tree
     * @param character The character within the tokens
     * @param tokens The tokens associated with the character.
     */
    public void xmlize(char character, Stack<Token> tokens) {
        if (tokens.size() == 0) {
            xml.offer(character);
        } else {
            boolean characterPushed = false;
            Queue<Token> queue = new LinkedList<Token>();
            queue.addAll(tokens);
            while (!queue.isEmpty()) {
                Token token = queue.poll();
                if (!token.isStartToken() && !characterPushed) {
                    xml.offer(character);
                    characterPushed = true;
                }
                if (token.isStartToken()) {
                    started.add(token);
                    xml.offer(token);
                } else {
                    if (started.contains(token.opposite())) {
                        started.remove(token.opposite());
                        xml.offer(token);
                    } else {
                        if (xml.contains(token)) {
                            xml.remove(xml.lastIndexOf(token));
                            xml.offer(token);
                        }
                    }
                }

            }
        }
    }

    /**
     * Resets all of the temporary structures. Still keeps the finalized
     * xml structures used for printing.
     */
    public void reset() {
        this.xml.clear();
        this.started.clear();
    }

    /**
     * Finalizes the current xml tree and saves it internally that can be
     * printed or saved later
     */
    public void finalizeXML() {
        List<String> xmlText = new LinkedList<String>();
        Queue xml_copy = new LinkedList();
        int indent = 0;

        //Remove any started and never ended tokens
        for (Token start : started) {
            int index = xml.lastIndexOf(start);
            xml.remove(index);
        }

        xml_copy.addAll(xml);

        while (!xml_copy.isEmpty()) {
            Object ob = xml_copy.poll();

            StringBuilder builder = new StringBuilder();

            if (ob instanceof Token) {
                if (((Token) ob).isStartToken()) {
                    for (int i = 0; i < indent; i++) {
                        builder.append("\t");
                    }
                    builder.append(ob);
                    indent++;
                } else {
                    indent--;
                    for (int i = 0; i < indent; i++) {
                        builder.append("\t");
                    }
                    builder.append(ob);
                }
            } else {
                for (int i = 0; i < indent; i++) {
                    builder.append("\t");
                }
                builder.append(ob);
            }

            xmlText.add(builder.toString());
        }
        savedXML.add(xmlText);
        this.reset();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (List<String> xmls : savedXML) {
            for (String xml : xmls) {
                builder.append(xml);
                builder.append("\n");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public void exportXML(String fileStart) throws IOException {
        for (List<String> xml : savedXML) {
            File xmlFile = new File(fileStart + "_" + savedXML.indexOf(xml) + ".xml");
            FileWriter fstream = new FileWriter(xmlFile);
            BufferedWriter out = new BufferedWriter(fstream);
            
            for(String str: xml) {
                out.write(str);
                out.write("\n");
            }
            
            out.close();
            fstream.close();
        }
    }
}
