

import java.util.*;

import static java.lang.Integer.max;

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
    static String s1;
    static String s2;

    int fromA;
    int fromB;
    int toA;
    int toB;
    int lengthA;
    int lengthB;
    double score;

    // match is variable
    //a match can be separated into two anchors
    public Anchor(SimpleChaining.Match match) {
        fromA = match.fromA;
        fromB = match.fromB;
        toA = match.toA;
        toB = match.toB;
        lengthA = match.getToA() - match.getFromA() + 1;
        lengthB = match.getToB() - match.getFromB() + 1;
        score = match.score;
    }

    private static Anchor toAnchor(SimpleChaining.Match match) {
        return new Anchor(match);
    }

    private static List<SimpleChaining.Match> selectAnchors() {
        //Set? matchSet = MatchFinding.findMatches();
        List<String> matchSet = new ArrayList<>();//idk??
        //simulated matchSet to act as output from MatchFinding algorithm
        matchSet.add("GGCTATG");
        matchSet.add("AGGCTATG");
        matchSet.add("GAGGCTATG");
        matchSet.add("AGAGGCTATG");
        matchSet.add("AAGAGGCTATG");
        matchSet.add("AAAGAGGCTATG");

        eliminateNoisyMatches(matchSet);
        sortMatches(matchSet);

        //run modified S&W;
        //TODO: modify S&W

        String s1 = "ACGTGTCAGTCAATATCA";
        String s2 = "AAGGATCGGGTAGC";
        SmithWaterman sw = new SmithWaterman(s1, s2);
        List<SimpleChaining.Match> matches = sw.getMatches();
        System.out.println(matches);

        double bestScore = sw.getAlignmentScore();
        for (int i = 0; i < matches.size(); i++) {
            if (matches.get(i).getScore() == bestScore) {
                //get best match
                SimpleChaining.Match match = matches.get(i);
            }
        }
        //get matchesArray frm MatchFinding..matchSet = matches fr. suffixTree
        matches.retainAll(new HashSet<>(matchSet)); //get intersection
        return matches;
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

    private static List<SimpleChaining.Match> sortMatches(List matchSet) {
        //run sequences through repeatMasker

        //get set difference
        List<SimpleChaining.Match> cleanMatches = getCleanMatches(matchSet);
        matchSet.removeAll(cleanMatches);
        List<SimpleChaining.Match> repeatMatches = new ArrayList<>();
        repeatMatches.addAll(matchSet);

        List<Integer> cleanAnchorsLengths = new ArrayList<>();
        List<Integer> repeatAnchorsLengths = new ArrayList<>();

        for (int i=0; i<cleanMatches.size(); i++) {
            Anchor a = toAnchor(cleanMatches.get(i));
            cleanAnchorsLengths.add(max(a.lengthA, a.lengthB));

        }

        for (int j=0; j<repeatMatches.size(); j++) {
            Anchor a = toAnchor(cleanMatches.get(j));
            repeatAnchorsLengths.add(max(a.lengthA, a.lengthB));
        }

//
//        Collections.sort(cleanAnchorsLengths, new LengthFirstComparator());
//        Collections.sort(repeatAnchorsLengths, new LengthFirstComparator());
        return cleanMatches;
    }


//    public double anchorScore(Anchor a) {
////        double u = a.score;
////        double v = a.length;
////        double w;
////
////        //w = vlogu if u >= 1 and v >= 5
////        // 0 otherwise
////
////        w = v * Math.log(u);
////        return w;
//    }

    public static List<SimpleChaining.Match> getCleanMatches(List<SimpleChaining.Match> matches) {
        List<SimpleChaining.Match> noOverlaps = new ArrayList<>();
        for (int i = 0; i < matches.size(); i++) {
            for (int j = i + 1; j < matches.size(); j++) {
                if (matches.get(i).notOverlap(matches.get(j))) {
                    noOverlaps.add(matches.get(i));
                }
            }
        }

        System.out.println(noOverlaps.size());
        return noOverlaps;
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
//        System.out.println(matchSet);
//        System.out.println(sortMatches(matchSet));
        System.out.println(selectAnchors());
        List<SimpleChaining.Match> matches = new ArrayList<>();
        matches.add(new SimpleChaining.Match(1, 7, 5,  8, 52.0));
        matches.add(new SimpleChaining.Match(8, 17, 9,  14, 70.0));
        getCleanMatches(matches);
    }

}
