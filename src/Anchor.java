

import java.util.*;

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

    public Anchor(int sequence1, int sequence2, int s1Start, int s2Start, int score) {
        s1 = sequence1;
        s2 = sequence2;

    }


    public List<Anchor> selectAnchors() {
        //Set? matchSet = MatchFinding.findMatches();
        List<String> matchSet = new ArrayList<>();
        eliminateNoisyMatches(matchSet);
        sortMatches(matchSet);
        //run modified S&W;
        //TODO: modify S&W
        String s1 = "";
        String s2 = "";
        SmithWaterman sw = new SmithWaterman(s1, s2);
        List<SimpleChaining.Match> matches = sw.getMatches();
        double bestScore = sw.getAlignmentScore();
        for (int i = 0; i < matches.size(); i++) {
            if (matches.get(i).getScore() == bestScore) {
                return (List<Anchor>) matches.get(i);
            }
        }
        return null;
    }

    private static List eliminateNoisyMatches(List matchSet) {
        List<String> nextMatchSet = new ArrayList<>();
        int longestMatchLength = 0;

        //REMOVE DUPS
        Set<String> hs = new HashSet<>();
        hs.addAll(matchSet);
        matchSet.clear();
        matchSet.addAll(hs);

        for (int i = 0; i < matchSet.size(); i++) {
            int compareTo = matchSet.get(i).toString().length();
            if (compareTo > longestMatchLength) {
                longestMatchLength = compareTo;
            }
        }

        for (int i = 0; i < matchSet.size(); i++) {
            if (matchSet.get(i).toString().length() < (longestMatchLength / 2)) {
                System.out.println(matchSet.get(i));
                nextMatchSet.add(matchSet.get(i).toString());
                matchSet.remove(i);
                i--;
            }
        }
        return matchSet;
    }

    private static List<String> sortMatches(List matchSet) {
        //run sequences through repeatMasker

        List<String> cleanMatches = new ArrayList<>();
        List<String> repeatMatches = new ArrayList<>();

        cleanMatches.add("happy");
        cleanMatches.add("log");
        cleanMatches.add("dog");
        cleanMatches.add("fog");
        cleanMatches.add("university");
        cleanMatches.add("matrix");
        cleanMatches.add("hi");
        Collections.sort(cleanMatches, new LengthFirstComparator());
        Collections.sort(repeatMatches, new LengthFirstComparator());
        return cleanMatches;
    }

    public double anchorScore(Anchor a) {
        double u = a.score;
        double v = a.length;
        double w;

        //w = vlogu if u >= 1 and v >= 5
        // 0 otherwise

        w = v * Math.log(u);
        return w;
    }

    public void anchorSequences(String s) {

    }

    public static class LengthFirstComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            if (o1.length()!=o2.length()) {
                return o1.length()-o2.length(); //overflow impossible since lengths are non-negative
            }
            return o1.compareTo(o2);
        }
    }

    public static void main(String[] args) {
        List<String> matchSet = new ArrayList<>();
        matchSet.add("hello");
        matchSet.add("you");
        matchSet.add("matchstick");
        matchSet.add("hippopotamus");
        //System.out.println(eliminateNoisyMatches(matchSet));
        System.out.println(matchSet);
        System.out.println(sortMatches(matchSet));
    }

}
