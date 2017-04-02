import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import java.util.Arrays;

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
        // two ways of doing things?
        //      1. take string 1, concatenate N, concatenate string 2?
        //          - bad run time?
        //      2. take strings and turn them into linked list, point end of seqOne to
        //         string N, then point that to beginning of seqTwo
        //          does this have any run time improvements?
     public static String concatenateSequences(String[] seqOne, String[] seqTwo){
         int seqOneLength = seqOne.length;
         int seqTwoLength = seqTwo.length;
         int newSeqLength = seqOneLength + seqTwoLength + 1;
         int counter = 0;

         String[] combinedArray = new String[newSeqLength];
         for (int i = 0; i < seqOneLength; i++){
             combinedArray[i] = seqOne[i];
         }
         combinedArray[seqOneLength] = "N";
         for (int j = seqOneLength+1; j < newSeqLength; j++){
             combinedArray[j] = seqTwo[counter];
             counter++;
         }

         return Arrays.toString(combinedArray);

     }

    //TODO: maximal repeated substring function
        // used once for each sequence

    //TODO: maximal matches function

    public static void main(String args[]) {
         String[] testerOne = {"D", "U", "N", "C", "A", "N"};
         String[] testerTwo = {"T", "R", "U", "O", "N", "G"};

         System.out.println(concatenateSequences(testerOne, testerTwo));

    }

}
