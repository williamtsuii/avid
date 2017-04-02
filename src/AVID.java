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

        //iteration:
        for (int j = 1; j<=S2.length(); j++) {
            for(int i = 1; i<=S1.length(); i++) {
                //j & i are inverted because going from left to right then top down

                //3 cases: top, left, and top left...
                //F[i-1][j-1] == top left; F[i][j-1] == top; F[i-1][j] == left
                double fromTop = F[i][j-1] - d;
                double fromLeft = F[i-1][j] - d;
                double fromTopLeft = F[i-1][j-1] + getBlosScore(S1, S2, i-1, j-1); //need to get blos score

                //Case 1
                if ((fromTopLeft >= fromTop) && (fromTopLeft >= fromLeft)) {
                    F[i][j] = fromTopLeft;
                }
                //Case 2
                else if ((fromTop >= fromLeft) && (fromTop >= fromTopLeft)) {
                    F[i][j] = fromTop;
                }
                //Case 3
                else if ((fromLeft >= fromTop) && (fromLeft >= fromTopLeft)) {
                    F[i][j] = fromLeft;

                }

            }
        }
        //print matrix
        for(double[] row : F) {
            printRow(row);
        }

    }

    public static void printRow(double[] row) {//for printing matrix
        for (double i : row) {
            System.out.print(i);
            System.out.print("\t");
        }
        System.out.println();
    }



    //TODO: If cases to use Anchor Selection / use trivial N&W (without heuristics)
        // use anchors ONLY if anchor set > 50% of length of the sequences aligned

    public static void main(String args[]) {
        NWAligner("EDKNPIDHNQVSQFLPETFAEQLIR", "IAGLCHDLGHGPFSHMFDGRF");
    }

}
