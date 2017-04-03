import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by william on 2017-03-29.
 */
public class Anchor {
    // this class is based on S & W algorithm
    private static int d = 0;
    private static int mismatch = (int) Double.POSITIVE_INFINITY;
    //1) first sequence involved (2) second sequence involved, (3) start of anchor in first sequence
    // (4) start of anchor in second sequence (5) length of anchor
    // (6) score of an anchor point (to prioritize)
    int s1; //sequence # of first sequence
    int s2;
    int s1Start;
    int s2Start;
    int length;
    int score;
    // match is variable


    // TODO: helper func. to remove matches < (longestMatch.length())/2 from init. consideration
    //this is a heuristic

    //TODO: helper func. to order anchor set


    //TODO: implement modified S&W alg.
    public Anchor(int sequence1, int sequence2) {
        s1 = sequence1;
        s2 = sequence2;
    }

    public Anchor anchorSelection() {
        //Set? matchSet = MatchFinding.findMatches();
        List<String> matchSet = new ArrayList<String>();
        eliminateNoisyMatches(matchSet);
        return null;
    }

    private void eliminateNoisyMatches(List matchSet) {
        int longestMatchLength;

        for (int i = 0; i < matchSet.size(); i++) {
            matchSet.get(i);
            //get length of string
        }
    }

    public void anchorSequences(String s) {

    }

    public static void main(String[] args) {

    }
}
