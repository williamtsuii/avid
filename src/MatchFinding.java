import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

/**
 * Created by william on 2017-04-01.
 */
public class MatchFinding extends Parser {

    Parser parsedFile = new Parser();
    String[] description = parsedFile.getDescription();
    String[] sequence = parsedFile.getSequence();
    public MatchFinding() {
        super();
    }

    //TODO: implement data structure for suffix tree

    //TODO: concatenating helper func.
        // place character 'N' between the two sequences

    //TODO: maximal repeated substring function
        // used once for each sequence

    //TODO: maximal matches function

    public static void main(String args[]) {

    }

}
