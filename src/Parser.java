import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by william on 2017-03-29.
 */
public class Parser {
    private static String[] description;
    private static String[] sequence;
    //this is to parse FASTA files

    //constructor
    private String input;
    public Parser() {
    }

    public static String[] getDescription() {
        return description;
    }

    public static String[] getSequence() {
        return sequence;
    }

    public static void seqGrep(String file) { //taken from UTexas
        List desc= new ArrayList();
        List seq = new ArrayList();
        try{
            BufferedReader in     = new BufferedReader(new FileReader(file));
            StringBuffer   buffer = new StringBuffer();
            String         line   = in.readLine();

            if( line == null )
                throw new IOException( file + " is an empty file" );

            if( line.charAt( 0 ) != '>' )
                throw new IOException( "First line of " + file + " should start with '>'" );
            else
                desc.add(line);
            for( line = in.readLine().trim(); line != null; line = in.readLine() )
            {
                if( line.length()>0 && line.charAt( 0 ) == '>' )
                {
                    seq.add(buffer.toString());
                    buffer = new StringBuffer();
                    desc.add(line);
                } else
                    buffer.append( line.trim() );
            }
            if( buffer.length() != 0 )
                seq.add(buffer.toString());
        }catch(IOException e)
        {
            System.out.println("Error when reading "+file);
            e.printStackTrace();
        }

        description = new String[desc.size()];
        sequence = new String[seq.size()];
        for (int i=0; i< seq.size(); i++)
        {
            description[i]=(String) desc.get(i);
            sequence[i]=(String) seq.get(i);
        }
    }


    public static void main(String[] args) {
        //seqGrep("./src/Q0VCA5.fasta.txt"); example

    }

}
