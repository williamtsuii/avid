import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

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

    /** Class Node **/
    static class Node {
        public int suffix_node;
        public static int Count = 1;

        /** Constructor **/
        public Node(){
            suffix_node = -1;
        }
    }

    /** Class Suffix Tree **/
    static class SuffixTree {

        private static final int MAX_LENGTH = 19000;
        private static final int HASH_TABLE_SIZE = 31179;
        private char[] T = new char[ MAX_LENGTH ];
        private int N;
        private Edge[] Edges ;
        private Node[] Nodes ;
        private Suffix active;
        String longestString = "";
        /** Class Suffix **/
        class Suffix
        {
            public int origin_node;
            public int first_char_index;
            public int last_char_index;
            /** Constructor **/
            public Suffix(int node, int start, int stop ){
                origin_node = node ;
                first_char_index = start ;
                last_char_index = stop;
            }
            /** Function Implicit  **/
            public boolean Implicit(){
                return first_char_index > last_char_index;
            }
            /** Function Explicit  **/
            public boolean Explicit() {
                return first_char_index > last_char_index;
            }
            /** Function Canonize()
             * A suffix in the tree is denoted by a Suffix structure
             * that denotes its last character.  The canonical
             * representation of a suffix for this algorithm requires
             * that the origin_node by the closest node to the end
             * of the tree.  To force this to be true, we have to
             * slide down every edge in our current path until we
             * reach the final node
             **/
            public void Canonize() {
                if (!Explicit() )
                {
                    Edge edge = Find( origin_node, T[ first_char_index ] );
                    int edge_span = edge.last_char_index - edge.first_char_index;
                    while ( edge_span <= ( last_char_index - first_char_index ) ) {
                        first_char_index = first_char_index + edge_span + 1;
                        origin_node = edge.end_node;
                        if ( first_char_index <= last_char_index )
                        {
                            edge = Find( edge.end_node, T[ first_char_index ] );
                            edge_span = edge.last_char_index - edge.first_char_index;
                        }
                    }
                }
            }
        }

        /** Class Edge **/
        class Edge {
            public int first_char_index;
            public int last_char_index;
            public int end_node;
            public int start_node;
            /** Constructor **/
            public Edge()
            {
                start_node = -1;
            }
            /** Constructor **/
            public Edge( int init_first, int init_last, int parent_node )
            {
                first_char_index = init_first;
                last_char_index = init_last;
                start_node = parent_node;
                end_node = Node.Count++;
            }
            /** function Insert ()
             *  A given edge gets a copy of itself inserted into the table
             *  with this function.  It uses a linear probe technique, which
             *  means in the case of a collision, we just step forward through
             *  the table until we find the first unused slot.
             **/
            public void Insert(){
                int i = Hash( start_node, T[ first_char_index ] );
                while ( Edges[ i ].start_node != -1 )
                    i = ++i % HASH_TABLE_SIZE;
                Edges[ i ] = this;
            }
            /** function SplitEdge ()
             *  This function is called
             *  to split an edge at the point defined by the Suffix argument
             **/
            public int SplitEdge( Suffix s )
            {
                Remove();
                Edge new_edge =  new Edge( first_char_index, first_char_index + s.last_char_index - s.first_char_index, s.origin_node );
                new_edge.Insert();
                Nodes[ new_edge.end_node ].suffix_node = s.origin_node;
                first_char_index += s.last_char_index - s.first_char_index + 1;
                start_node = new_edge.end_node;
                Insert();
                return new_edge.end_node;
            }
            /** function Remove ()
             *  This function is called to remove an edge from hash table
             **/
            public void Remove()
            {
                int i = Math.abs(Hash( start_node, T[ first_char_index ] ));
                while ( Edges[ i ].start_node != start_node ||
                        Edges[ i ].first_char_index != first_char_index )
                    i = ++i % HASH_TABLE_SIZE;
                for ( ; ; )
                {
                    Edges[ i ].start_node = -1;
                    int j = i;
                    for ( ; ; )
                    {
                        i = ++i % HASH_TABLE_SIZE;
                        if ( Edges[ i ].start_node == -1 )
                            return;
                        int r = Hash( Edges[ i ].start_node, T[ Edges[ i ].first_char_index ] );
                        if ( i >= r && r > j )
                            continue;
                        if ( r > j && j > i )
                            continue;
                        if ( j > i && i >= r )
                            continue;
                        break;
                    }
                    Edges[ j ] = Edges[ i ];
                }
            }
        }



        /** Constructor */

        public SuffixTree()

        {
            Edges = new Edge[ HASH_TABLE_SIZE ];
            for (int i = 0; i < HASH_TABLE_SIZE; i++)
                Edges[i] = new Edge();
            Nodes = new Node[ MAX_LENGTH * 2 ];
            for (int i = 0; i < MAX_LENGTH * 2 ; i++)
                Nodes[i] = new Node();
            active = new Suffix( 0, 0, -1 );
        }

        /** Function Find() - function to find an edge **/
        public Edge Find( int node, int c ){
            int i = Hash( node, c );
            for ( ; ; )
            {
                if ( Edges[ i ].start_node == node )
                    if ( c == T[ Edges[ i ].first_char_index ] )
                        return Edges[ i ];
                if ( Edges[ i ].start_node == -1 )
                    return Edges[ i ];
                i = ++i % HASH_TABLE_SIZE;
            }
        }

        /** Function Hash() - edges are inserted into the hash table using this hashing function **/
        public static int Hash( int node, int c ){
            return (( node << 8 ) + c ) % HASH_TABLE_SIZE;
        }
        /** Function AddPrefix() - called repetitively, once for each of the prefixes of the input string **/
        public void AddPrefix( Suffix active, int last_char_index )
        {
            int parent_node;
            int last_parent_node = -1;
            for ( ; ; )
            {
                Edge edge;
                parent_node = active.origin_node;
                if ( active.Explicit() ){
                    edge = Find( active.origin_node, T[ last_char_index ] );
                    if ( edge.start_node != -1 )
                        break;
                }
                else{
                    edge = Find( active.origin_node, T[ active.first_char_index ] );
                    int span = active.last_char_index - active.first_char_index;
                    if ( T[ edge.first_char_index + span + 1 ] == T[ last_char_index ] )
                        break;
                    parent_node = edge.SplitEdge( active );
                }
                Edge new_edge = new Edge( last_char_index, N, parent_node );
                new_edge.Insert();
                if ( last_parent_node > 0 )
                    Nodes[ last_parent_node ].suffix_node = parent_node;
                last_parent_node = parent_node;
                if ( active.origin_node == 0 )
                    active.first_char_index++;
                else
                    active.origin_node = Nodes[ active.origin_node ].suffix_node;
                active.Canonize();
            }
            if ( last_parent_node > 0 )
                Nodes[ last_parent_node ].suffix_node = parent_node;
            active.last_char_index++;
            active.Canonize();
        }
        /** Function to print all contents and details of suffix tree **/
        public void dump_edges(int current_n ){
            System.out.println(" Start  End  Suf  First Last  String\n");
            for ( int j = 0 ; j < HASH_TABLE_SIZE ; j++ )
            {
                Edge s = Edges[j];
                if ( s.start_node == -1 )
                    continue;
                System.out.printf("%5d %5d %3d %5d %6d   ", s.start_node , s.end_node, Nodes[ s.end_node ].suffix_node, s.first_char_index, s.last_char_index);
                int top;
                if ( current_n > s.last_char_index )
                    top = s.last_char_index;
                else
                    top = current_n;
                for ( int l = s.first_char_index ; l <= top; l++)
                    System.out.print( T[ l ]);
                System.out.println();
            }
        }

//        public void subSequence(){
//
//            ArrayList<Edge> listofEdges = new ArrayList<Edge>();
//            for (int i = 0; i < HASH_TABLE_SIZE; i++){
//                Edge s = Edges[i];
//                if (s.start_node == -1){
//                    continue;
//                }
//                listofEdges.add(s);
//            }
//            char[] stringToCheck = new char[listofEdges.size()];
//            int currentLongest = 0;
//            for (int j = 0; j < listofEdges.size(); j++){
//                Edge s = listofEdges.get(j);
//                if (Nodes[s.end_node].suffix_node == -1){
//                    continue;
//                }
//                else {
//                    int top =  s.last_char_index;
//                    int count = 0;
//                    for (int l = s.first_char_index; l <= top; l++) {
//                        stringToCheck[0] = T[l];
//                        count++;
//                    }
//                    if (stringToCheck.length > currentLongest) {
//                        currentLongest = stringToCheck.length;
//                    }
//
//                    if (listofEdges.get(j).start_node == listofEdges.get(j+1).end_node){
//                        if (Nodes[s.end_node].suffix_node == -1){
//                            continue;
//                        }
//                        top = listofEdges.get(j+1).last_char_index;
//                        for (int y = listofEdges.get(j+1).first_char_index; y <= top; y++){
//                            stringToCheck[count] = T[y];
//                        }
//                    }
////
//
//
//                }
//            }
//
//        }

        public void subSequence_2(){
            ArrayList<Edge> listofEdges = new ArrayList<Edge>();
            for (int i = 0; i < HASH_TABLE_SIZE; i++){
                Edge s = Edges[i];
                if (s.start_node == -1){
                    continue;
                }
                listofEdges.add(s);
            }
            subSequenceHelper(listofEdges);
        }

        public String getStringFromEdge(Edge e){
            StringBuilder sb = new StringBuilder();
            String returnString;
            for (int i = e.first_char_index; i <= e.last_char_index; i++){
                sb.append(T[i]);
            }
            returnString = sb.toString();
            return returnString;
        }

        // helper function may have to do check first = last, repeat and build the sequences



        public ArrayList<Edge> getIndexTrail(ArrayList<Edge> usefulEdges, ArrayList<Edge> allEdges){
            ArrayList<Edge> toReturn = new ArrayList<>();
            for (Edge e : allEdges){
                toReturn.add(e);
            }
            ArrayList<Edge> toCheck = usefulEdges;
            if (toCheck.size() > 1) {
                for (int i = 0; i < toCheck.size(); i++) {
                    Edge s = toCheck.get(i);
                    if (s.start_node == toCheck.get(i + 1).start_node) {
                        if (getStringFromEdge(s).length() > getStringFromEdge(toCheck.get(i+1)).length()){
                            toCheck.remove(i+1);
                        }
                        else toCheck.remove(i);
                    }
                }
            }
            for (int i = 0; i < usefulEdges.size(); i++){
                Edge s = toCheck.get(i);
                for (int j = 0; j < toReturn.size(); j++){
                    if (s.end_node == toReturn.get(j).start_node){
                        toCheck.add(toReturn.get(j));
                    }
                }
            }
            return toCheck;
        }

        public String buildStringFromIndexes(ArrayList<Edge> toCheck){
            String str = "";
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < toCheck.size(); i++){
                Edge s = toCheck.get(i);
                sb.append(getStringFromEdge(s));
            }
            str = sb.toString();
            System.out.println("STRING: " + str);
            return str;
        }

        // needs to recurse, build the string and remove the edge that we took the string from
        // once empty, compare/keep max string
        //
        public String followTheRabbitHole(Edge s, ArrayList<Edge> allEdges) {
            String builtString = "";
            String tempString = getStringFromEdge(s);
            int currentMax = 0;
            ArrayList<Edge> listWithSameStartEnd = new ArrayList<Edge>();

            for (int i = 0; i < allEdges.size(); i++) {
                if (s.end_node == allEdges.get(i).start_node) {
                    listWithSameStartEnd.add(allEdges.get(i));
                }
            }
            if (listWithSameStartEnd.size() == 0){
                tempString = getStringFromEdge(s);
                if (tempString.length() > currentMax){
                    return tempString;
                }
            }
            for (int i = 0; i < listWithSameStartEnd.size(); i++){
                longestString = (findLongest(followTheRabbitHole(listWithSameStartEnd.get(i), allEdges)));
                tempString = getStringFromEdge(s).concat(longestString);
                if (tempString.length() > builtString.length()){
                    builtString = tempString;
                    currentMax = builtString.length();
                }
            }
            longestString = "";
            return builtString;
        }

        public String findLongest(String str){
            if (str.length() > longestString.length()){
                return str;
            }
            else return longestString;
        }

        public void subSequenceHelper(ArrayList<Edge> listOfEdges){
            ArrayList<Edge> newList = new ArrayList<>();
            for (Edge e : listOfEdges){
                if (Nodes[e.end_node].suffix_node != -1){
                    newList.add(e);
                }
            }
            String temp = "";
            int current = 0;
            String longestSoFar = "";
            String first = "";
            ArrayList<Edge> listWithSameStartEnd = new ArrayList<>();
            for (int i = 0; i < newList.size(); i++) {
                Edge s = newList.get(i);
                first = getStringFromEdge(s);

//                if (getStringFromEdge(s).length() > longestSoFar.length()) {
//                    longestSoFar = getStringFromEdge(s);
//
//                }
//                for (int j = i+1; j < newList.size(); j++) {
//                    if (s.end_node == newList.get(j).start_node) {
//                        listWithSameStartEnd.add(newList.get(j));
//                    }
//                }
//                if (listWithSameStartEnd.size() == 0){
//                    continue;
//                }
//                ArrayList<Edge> keepTrack = getIndexTrail(listWithSameStartEnd, newList);
//                temp = buildStringFromIndexes(keepTrack);
                temp = followTheRabbitHole(s, newList);
                if (temp.length() > longestSoFar.length()){
                    longestSoFar = temp;
                }
//                    for (Edge e : keepTrack){

//                        temp = getStringFromEdge(e);
//                        System.out.println("this is edge" + e + "string" + temp);
//                        if (temp.length() > current) {
//                            current = (longestSoFar.length() + temp.length());
//                            longestSoFar = first + temp;
//                        }
//                    }
//                longestSoFar = getStringFromEdge(s) + remember;

                listWithSameStartEnd.clear();
            }
                System.out.println(longestSoFar);

//                for (int k = s.first_char_index; k <= s.last_char_index; k++){
//                    sb.append(T[k]);
//                }
//                temp = sb.toString();
//                if (temp.length() > current){
//                    current = temp.length();
//                }
//                int m = i;



//                while ((s.end_node == newList.get(m+1).end_node) && ((m+1) <= newList.size())) {
//                    for (int k = newList.get(m+1).first_char_index; k <= newList.get(m+1).last_char_index; k++){
//                        sb.append(T[k]);
//                    }
//                    temp = sb.toString();
//
//                    current = temp.length();
//                }
        }

        public void doTraversal(){
            int currentMax = 0;
            int index = 0;
            boolean extra = false;
            int remembered = 0;
            int[] toAdd = new int[Edges.length];

            for (int i = 0; i < Edges.length; i++){
                Edge e = Edges[i];

                if (Nodes[e.end_node].suffix_node == -1){
                    continue;
                }


                if ((Edges[i].last_char_index - Edges[i].first_char_index) == 0){
                       extra = true;
                       remembered = Edges[i].last_char_index;
                }
//                if ((Edges[i].last_char_index > ))

                if (Math.abs(Edges[i].last_char_index - Edges[i].first_char_index) > currentMax){
                    currentMax = Math.abs(Edges[i].last_char_index - Edges[i].first_char_index);
                    index = i;
                }
            }
            System.out.println("BREAK");
            Edge s = Edges[index];
            System.out.printf("%5d %5d %3d %5d %6d   ", s.start_node , s.end_node, Nodes[ s.end_node ].suffix_node, s.first_char_index, s.last_char_index);
            if (extra) {
                System.out.print(T[remembered]);
            }
            int top = s.last_char_index;
            for ( int l = s.first_char_index ; l <= top; l++)
                System.out.print( T[ l ]);
            System.out.println();

        }

//        public void longestSubstring(String str){
//            char[] holder = new char[HASH_TABLE_SIZE];
//            int currentLongest = 0;
//            int count = 0;
//            String ans = "";
//            String temp = "";
//            char[] tempArray = new char[HASH_TABLE_SIZE];
////            char[] ans = new char[str.length()];
//            String finalans = "";
//            for (int i = 0; i < HASH_TABLE_SIZE; i++){
//                Edge e = Edges[i];
//                if (e.start_node == -1){
//                    continue;
//                }
//                int top = e.last_char_index;
//                for (int x = e.first_char_index; x <= top; x++){
//                    tempArray[ = new String(T[x]);
//                }
//
//
//                for (int j = 1; j < HASH_TABLE_SIZE; j++){
//                    if (e.end_node == Edges[j].start_node) {
//                        if (Nodes[(Edges[j]).end_node].suffix_node == -1) {
//                            continue;
//                        }
//                        int top = e.last_char_index;
//                        for (int k = e.first_char_index; k <= top; k++){
//                            System.out.print(T[k]);
//                        }
//                        int top2 = Edges[j].last_char_index;
//                        for (int l = Edges[j].first_char_index; l <= top2; l++){
//                            System.out.print(T[l]);
//                        }
//                        if (holder.length > currentLongest) {
//                            currentLongest = holder.length;
//                        }
//                    }
//                }
//
//            }
////            finalans = String.valueOf(holder);
////            System.out.println("BREAK");
//            System.out.println();
//        }



    }
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
         StringBuilder builder = new StringBuilder();
         for(String s : combinedArray) {
             builder.append(s);
         }
         return builder.toString();

     }

    //TODO: maximal repeated substring function
        // used once for each sequence

    //TODO: maximal matches function

    public static void main(String args[]) throws IOException {
         String[] testerOne = {"D", "U", "N", "C", "A", "N"};
         String[] testerTwo = {"T", "R", "U", "O", "N", "G", "V", "U", "O", "N"};

        System.out.println(concatenateSequences(testerOne, testerTwo));
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        System.out.println("Suffix Tree Test\n");
//        System.out.println("Enter string\n");
//        String str = br.readLine();
//        String str = concatenateSequences(testerOne, testerTwo);
        String str = "GATTAGA$";
//        String str = "pqrpqpqabab$";
//        String str = "ABABABA$";
//        String str = "banana$";
//        String str = "GEEKSFORGEEKS$";
//        String str = "ATCGATCGA$";
//        String str = "ACTGGTAGATCAGGTA$";
//        String str = "ACTGTAACTGTAACT$";
        /** Construct Suffix Tree **/
        SuffixTree st = new SuffixTree();
        st.T = str.toCharArray();
        st.N = st.T.length - 1;
        int x = 0;
        for (int i = 0 ; i <= st.N ; i++ )
            st.AddPrefix( st.active, i );
        st.dump_edges( st.N );
        st.subSequence_2();

    }

}
