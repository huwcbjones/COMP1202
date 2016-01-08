package ZooSim.Parser;

/**
 * Static class containing token types that can be returned by the Lexer
 *
 * @author Huw Jones
 * @since 23/10/2015
 */
public class Tokens {
    /**
     * Undefined
     */
    public static final String UNDEFINED = "UNDEFINED";

    /**
     * End Of Line
     */
    public static final String EOL = "EOL";

    /**
     * End Of File
     */
    public static final String EOF = "EOF";

    /**
     * Colon (:)
     */
    public static final String COLON = "COLON";

    /**
     * Semicolon (;)
     */
    public static final String SEMICOLON = "SEMICOLON";

    /**
     * Full stop (.)
     */
    public static final String STOP = "STOP";

    /**
     * Comma (,)
     */
    public static final String COMMA = "COMMA";

    /**
     * Greater Than (&gt;)
     */
    public static final String GT = "GT";

    /**
     * Greater Than or Equal to (&gt;=)
     */
    public static final String GTE = "GTE";

    /**
     * Less Than (&lt;)
     */
    public static final String LT = "LT";

    /**
     * Less Than or Equal to (&lt;=)
     */
    public static final String LTE = "LTE";

    /**
     * Hash (#)
     */
    public static final String HASH = "HASH";

    /**
     * Add/Plus (+)
     */
    public static final String PLUS = "PLUS";

    /**
     * Subtract/Minus (-)
     */
    public static final String MINUS = "MINUS";

    /**
     * Equality (==)
     */
    public static final String EQUAL = "EQUAL";

    /**
     * Assignment (=)
     */
    public static final String ASSIGN = "ASSIGN";

    /**
     * An Integer. Matches /\d/
     */
    public static final String INTEGER = "INTEGER";

    /**
     * An string of chars. Starts with an alphabetical letter. Matches /^&#91;A-Za-z&#92;+&#91;A-Za-z\d&#92;*$/
     */
    public static final String CHAR = "CHAR";


}
