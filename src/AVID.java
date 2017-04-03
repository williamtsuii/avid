/**
 * Created by william on 2017-04-01.
 */

public class AVID {
    // this class is based on N & W algorithm
    private static double d = 1.5;
    private static int match = 3;
    private static int mismatch = 2;
    private static double[][] F;
    private static char[] nucleotideSequence = {'A', 'R', 'N', 'D', 'C', 'Q', 'E', 'G', 'H', 'I', 'L', 'K', 'M',
                                                'F', 'P', 'S', 'T', 'W', 'Y', 'V', 'B', 'J', 'Z', 'X'};
    private static int[][] blos50 = {
            /*A,  R,  N,  D,  C,  Q,  E,  G,  H,  I,  L,  K,  M,
             F,  P,  S,  T,  W,  Y,  V,  B,  J,  Z,  X,  *        */
    /*A*/    {5, -2, -1, -2, -1, -1, -1,  0, -2, -1, -2, -1, -1,
            -3, -1,  1,  0, -3, -2,  0, -2, -2, -1, -1, -5},
    /*R*/   {-2,  7, -1, -2, -4,  1,  0, -3,  0, -4, -3,  3, -2,
            -3, -3, -1, -1, -3, -1, -3, -1, -3,  0, -1, -5},
    /*N*/   {-1, -1,  7,  2, -2,  0,  0,  0,  1, -3, -4,  0, -2,
            -4, -2,  1,  0, -4, -2, -3,  5, -4,  0, -1, -5},
    /*D*/  {-2, -2,  2,  8, -4,  0,  2, -1, -1, -4, -4, -1, -4,
            -5, -1,  0, -1, -5, -3, -4,  6, -4,  1, -1, -5},
    /*C*/  {-1, -4, -2, -4, 13, -3, -3, -3, -3, -2, -2, -3, -2,
            -2, -4, -1, -1, -5, -3, -1, -3, -2, -3, -1, -5},
    /*Q*/   {-1,  1,  0,  0, -3,  7,  2, -2,  1, -3, -2,  2,  0,
            -4, -1,  0, -1, -1, -1, -3,  0, -3,  4, -1, -5},
    /*E*/   {-1,  0,  0,  2, -3,  2,  6, -3,  0, -4, -3,  1, -2,
            -3, -1, -1, -1, -3, -2, -3,  1, -3,  5, -1, -5},
    /*G*/   { 0, -3,  0, -1, -3, -2, -3,  8, -2, -4, -4, -2, -3,
            -4, -2,  0, -2, -3, -3, -4, -1, -4, -2, -1, -5},
    /*H*/   {-2,  0,  1, -1, -3,  1,  0, -2, 10, -4, -3,  0, -1,
            -1, -2, -1, -2, -3,  2, -4,  0, -3,  0, -1, -5},
    /*I*/  { -1, -4, -3, -4, -2, -3, -4, -4, -4,  5,  2, -3,  2,
            0, -3, -3, -1, -3, -1,  4, -4,  4, -3, -1, -5},
    /*L*/   {-2, -3, -4, -4, -2, -2, -3, -4, -3,  2,  5, -3,  3,
            1, -4, -3, -1, -2, -1,  1, -4,  4, -3, -1, -5},
    /*K*/   {-1,  3,  0, -1, -3,  2,  1, -2,  0, -3, -3,  6, -2,
            -4, -1,  0, -1, -3, -2, -3,  0, -3,  1, -1, -5},
    /*M*/  {-1, -2, -2, -4, -2,  0, -2, -3, -1,  2,  3, -2,  7,
            0, -3, -2, -1, -1,  0,  1, -3,  2, -1, -1, -5},
    /*F*/   {-3, -3, -4, -5, -2, -4, -3, -4, -1,  0,  1, -4,  0,
            8, -4, -3, -2,  1,  4, -1, -4,  1, -4, -1, -5},
    /*P*/  { -1, -3, -2, -1, -4, -1, -1, -2, -2, -3, -4, -1, -3,
            -4, 10, -1, -1, -4, -3, -3, -2, -3, -1, -1, -5},
    /*S*/   { 1, -1,  1,  0, -1,  0, -1,  0, -1, -3, -3,  0, -2,
            -3, -1,  5,  2, -4, -2, -2,  0, -3,  0, -1, -5},
    /*T*/   { 0, -1,  0, -1, -1, -1, -1, -2, -2, -1, -1, -1, -1,
            -2, -1,  2,  5, -3, -2,  0,  0, -1, -1, -1, -5},
    /*W*/  { -3, -3, -4, -5, -5, -1, -3, -3, -3, -3, -2, -3, -1,
            1, -4, -4, -3, 15,  2, -3, -5, -2, -2, -1, -5},
    /*Y*/   {-2, -1, -2, -3, -3, -1, -2, -3,  2, -1, -1, -2,  0,
            4, -3, -2, -2,  2,  8, -1, -3, -1, -2, -1, -5},
    /*V*/   { 0, -3, -3, -4, -1, -3, -3, -4, -4,  4,  1, -3,  1,
            -1, -3, -2,  0, -3, -1,  5, -3,  2, -3, -1, -5},
    /*B*/   {-2, -1,  5,  6, -3,  0,  1, -1,  0, -4, -4,  0, -3,
            -4, -2,  0,  0, -5, -3, -3,  6, -4,  1, -1, -5},
    /*J*/   {-2, -3, -4, -4, -2, -3, -3, -4, -3,  4,  4, -3,  2,
            1, -3, -3, -1, -2, -1,  2, -4,  4, -3, -1, -5},
    /*Z*/   {-1,  0,  0,  1, -3,  4,  5, -2,  0, -3, -3,  1, -1,
            -4, -1,  0, -1, -2, -2, -3,  1, -3,  5, -1, -5},
    /*X*/   {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -5},
            {  -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5,
                    -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5,  1}
    };
    private static String S1T;
    private static String S2T;
    private static String[] alignments;
    private AVID() {
        super();
    }

    //TODO: F Matrix init:
    public static void initFMAtrix(String S1, String S2) {
        F = new double[S1.length()+1][S2.length()+1];
        //F[i][0] and F[0][j] are initialized as all gap scores, so need +1
        for (int i = 0; i <= S1.length(); i++) { //init x axis
            F[i][0] = i*(0-d);
        }
        for (int i = 0; i <= S2.length(); i++) { //init y axis
            F[0][i] = i*(0-d);
        }
    }

    //TODO: get blos50 score
    public static double getBlosScore(String S1, String S2, int i, int j) {
        char[] S1Array = S1.toCharArray();
        char[] S2Array = S2.toCharArray();
        double blosScore;
        int blosPos1 = -1;
        int blosPos2 = -1;

        for (int k = 0; k < nucleotideSequence.length; k++) {
            if (nucleotideSequence[k] == S1Array[i]) {
                blosPos1 = k;
                break;
            }

        }
        for (int k = 0; k < nucleotideSequence.length; k++) {
            if (nucleotideSequence[k] == S2Array[j]) {
                blosPos2 = k;
                break;
            }

        }

        blosScore = blos50[blosPos1][blosPos2];
        return blosScore;
    }

    //TODO: N&W Algorithm
    public static void NWAligner(String S1, String S2) {
        initFMAtrix(S1, S2);
        S1T = S1; S2T = S2;
        int m = S1.length();
        int n = S2.length();
        byte[][] pointers = new byte[m+1][n+1];

        //iteration:
        for (int j = 1; j<=S2.length(); j++) {
            for(int i = 1; i<=S1.length(); i++) {
                //j & i are inverted because going from left to right then top down

                //3 cases: top, left, and top left...
                //F[i-1][j-1] == top left; F[i][j-1] == top; F[i-1][j] == left
                double fromTop = F[i][j - 1] - d;
                double fromLeft = F[i - 1][j] - d;
                double fromTopLeft = F[i - 1][j - 1] + getBlosScore(S1, S2, i - 1, j - 1); //need to get blos score

                //Case 1
                if ((fromLeft >= fromTop) && (fromLeft >= fromTopLeft)) {
                    F[i][j] = fromLeft;
                    pointers[i][j] = 1;
                }
                //Case 2
                else if ((fromTopLeft >= fromTop) && (fromTopLeft >= fromLeft)) {
                    F[i][j] = fromTopLeft;
                    pointers[i][j] = 2;
                }
                //Case 3
                else if ((fromTop >= fromLeft) && (fromTop >= fromTopLeft)) {
                    F[i][j] = fromTop;
                    pointers[i][j] = 3;
                }



            }
        }
        //print matrix
        for(double[] row : F) {
            printRow(row);
        }

        getAlignment(pointers, n, m);
    }

    public static void printRow(double[] row) {//for printing matrix
        for (double i : row) {
            System.out.print(i);
            System.out.print("\t");
        }
        System.out.println();
    }

    //TODO: traceback
    public static Object getTraceback(byte[][] pointers, int n, int m) {
        StringBuffer alignment1Buffer = new StringBuffer();
        StringBuffer alignment2Buffer = new StringBuffer();
        char[] S1A = S1T.toCharArray();
        char[] S2A = S2T.toCharArray();
        char c1; char c2;
        int len1 = 0; int len2 = 0;
        int maxlen = S1A.length + S2A.length;
        char[] reversed1 = new char[maxlen];
        char[] reversed2 = new char[maxlen];

        //initialize traceback matrix: up=3; left=1; topleft =2;
        pointers[0][0] = 0;
        for (int i = 1; i <= m; i++) {
            pointers[i][0] = 3;
        }
        for (int j = 1; j <= n; j++) {
            pointers[0][j] = 1;
        }

        //iteration:
        boolean stillGoing = true;
        while (stillGoing) {
            switch (pointers[m][n]) {
                case 3: //top
                    reversed1[len1++] = S1A[--m];
                    reversed2[len2++] = '-';
                    break;
               case 2: //diagonal
                    c1 = S1A[--m];
                    c2 = S2A[--n];
                    reversed1[len1++] = c1;
                    reversed2[len2++] = c2;
                    break;
                case 1: //left
                    reversed1[len1++] = '-';
                    reversed2[len2++] = S2A[--n];
                    break;
                case 0:
                    stillGoing = false;
            }
        }

        for(byte[] row : pointers) {
            printPointers(row);
        }

        String[] alignments = new String[] {
               String.valueOf(reversed1),
                String.valueOf(reversed2)
        };
        return alignments; // stub
    }

    public static void printPointers(byte[] row) {//for printing matrix
        for (byte i : row) {
            System.out.print(i);
            System.out.print("\t");
        }
        System.out.println();
    }

    public static String[] getAlignment(byte[][] pointers, int n, int m) {
        alignments = (String[]) getTraceback(pointers, n, m);
        for (int i = 0; i < alignments.length; i++) {
            System.out.println(alignments[i]);
        }
        return alignments;
    }

    //TODO: If cases to use Anchor Selection / use trivial N&W (without heuristics)
        // use anchors ONLY if anchor set > 50% of length of the sequences aligned

    public static void main(String args[]) {
        NWAligner("EDKNPIDHNQVSQFLPETFAEQLIR", "IAGLCHDLGHGPFSHMFDGRF"); //example
    }

}
