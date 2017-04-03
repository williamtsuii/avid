/**
 * Created by Gora on 2017-04-03.
 */
public class InvalidAlphabetException extends RuntimeException {

    public InvalidAlphabetException (char alphabet, String message) {
        super ("Alphabet " + alphabet + ": " + message) ;
    }

    public InvalidAlphabetException (String message) {
        super (message) ;
    }
}
