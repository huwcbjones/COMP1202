package ZooSim.Parser;

import java.text.ParseException;

/**
 * A Tokeniser. Parses and tokenises the contents of a string.
 *
 * @author Huw Jones
 * @since 23/10/2015
 */
public class Lexer {

    private String _fileName = "";
    private String _text;
    private int _pos = 0;
    private int _lineCharPos = 0;
    private int _linePos = 1;
    private int _lineOffset = 0;
    private boolean _isEOF = false;
    private char _currentChar;

    public Lexer() {
        this._isEOF = true;
    }

    public Lexer(String text) {
        this._text = text;
        this._currentChar = _text.charAt(_pos);
    }

    public Lexer(String text, String filename) {
        this._fileName = filename;
        this._text = text;
        this._currentChar = _text.charAt(_pos);
    }

    /**
     * Returns boolean indicating whether char is an alphanumeric character
     *
     * @param test Char to test
     * @return true if char is alphanumeric
     */
    public static boolean isAlpha(char test) {
        return ((Character) test).toString().matches("^[\\dA-z]$");
    }

    /**
     * Sets text for lexer to process (useful for interactive mode(s))
     *
     * @param text Text to process.
     */
    public void setText(String text) {
        this._text = text;
        this._isEOF = false;
        this.reset();
    }

    /**
     * Resets lexer position to line 1, char 0
     */
    public void reset() {
        this._pos = 0;
        _linePos = 1;
        _lineCharPos = 0;
        if (_text.length() <= _pos) {
            this._isEOF = true;
        } else {
            this._isEOF = false;
            this._currentChar = _text.charAt(_pos);
        }
    }

    /**
     * Sets the line offset (useful for chaining lexers together and recurse loops)
     *
     * @param offset Amount of lines to offset by
     */
    public void setLineOffset(int offset) {
        this._lineOffset = offset;
    }

    /**
     * Returns current filename (if provided)
     *
     * @return Filename, or blank string
     */
    public String getFileName() {
        return this._fileName;
    }

    /**
     * Returns current line number
     *
     * @return Line number
     */
    public int getLineNumber() {
        return _linePos + _lineOffset;
    }

    /**
     * Returns position in current line
     *
     * @return Line position
     */
    public int getLinePosition() {
        return _lineCharPos + 1;
    }

    /**
     * Parses and returns next token. Automatically skips newline chars.
     *
     * @return Token containing value and token type
     * @throws ParseException Thrown if token is invalid/unrecognised
     */
    public Token getNextToken() throws ParseException {
        return getNextToken(true);
    }

    /**
     * Parses and returns next token.
     *
     * @param returnEOL If set to true, newlines will <b>not</b> be skipped
     * @return Token containing value and token type
     * @throws ParseException Thrown if token is invalid/unrecognised
     */
    public Token getNextToken(boolean returnEOL) throws ParseException {
        // Exit out of loop if we hit EOF
        while (!this._isEOF) {
            // Skip whitespace
            if (this._currentChar == ' ') {
                this._skipWhitespace();
                continue;
            }
            // Check for newline, and skipping newline
            if (this._currentChar == '\n' && !returnEOL) {
                this._skipLineBreaks();
                continue;

                // Check for newline (but not skipping the char)
            } else if (this._currentChar == '\n') {
                // Reset line pos
                this._linePos++;
                this._lineCharPos = 0;
                this.advance();
                return new Token(Tokens.EOL, "\n");
            }

            // Check for equals and assign
            if (this._currentChar == '=') {
                this.advance();
                if (this._currentChar == '=') {
                    return new Token(Tokens.ASSIGN, "==");
                } else {
                    return new Token(Tokens.EQUAL, "=");
                }
            }

            // Check for Greater Than
            if (this._currentChar == '>') {
                this.advance();
                // Check for Greater Than and Equal to
                if (this._currentChar == '=') {
                    return new Token(Tokens.GTE, ">=");
                } else {
                    return new Token(Tokens.GT, ">");
                }
            }

            // Check for Less Than
            if (this._currentChar == '<') {
                this.advance();
                // Check for Less Than and Equal to
                if (this._currentChar == '=') {
                    return new Token(Tokens.LTE, "<=");
                } else {
                    return new Token(Tokens.LT, "<");
                }
            }

            // Check digit (integer)
            if (Lexer.isDigit(this._currentChar)) {
                return new Token(Tokens.INTEGER, this._getInteger());
            }

            // Check for string
            if (Lexer.isChar(this._currentChar)) {
                return new Token(Tokens.CHAR, this._getString());
            }

            // Check for comma
            if (this._currentChar == ',') {
                this.advance();
                return new Token(Tokens.COMMA, ",");
            }

            // CHeck for colon
            if (this._currentChar == ':') {
                this.advance();
                return new Token(Tokens.COLON, ":");
            }

            // Check for semi colon
            if (this._currentChar == ';') {
                this.advance();
                return new Token(Tokens.SEMICOLON, ";");
            }

            // Handle undefined tokens
            Token udef = new Token(Tokens.UNDEFINED, this._currentChar);
            throw new ParseException("Unknown token, " + udef.toString() + ". On line " + _linePos + ", char " + _lineCharPos, 0);
        }

        // Reached end of file
        return new Token(Tokens.EOF, "EOF");
    }

    /**
     * Skips through whitespace
     */
    private void _skipWhitespace() {
        while (!this._isEOF && this._currentChar == ' ') {
            this.advance();
        }
    }

    /**
     * Skips through line breaks
     */
    private void _skipLineBreaks() {
        while (!this._isEOF && this._currentChar == '\n') {
            this._linePos++;
            this._lineCharPos = 0;
            this.advance();
        }
    }

    /**
     * Advances the lexer on one character
     */
    public void advance() {
        this._pos++;
        this._lineCharPos++;
        if (this._pos > _text.length() - 1) {
            _isEOF = true;
        } else {
            _currentChar = _text.charAt(_pos);
        }
    }

    /**
     * Returns boolean indicating whether char is a digit
     *
     * @param test Char to test
     * @return true if char is a digit
     */
    public static boolean isDigit(char test) {
        return ((Character) test).toString().matches("^\\d$");
    }

    /**
     * Parses and returns an integer
     *
     * @return Parsed integer
     */
    private int _getInteger() {
        StringBuilder integer = new StringBuilder();
        while (!this._isEOF && Lexer.isDigit(this._currentChar)) {
            integer.append(this._currentChar);
            this.advance();
        }
        return Integer.parseInt(integer.toString());
    }

    /**
     * Returns boolean indicating whether char is an alphabetic character
     *
     * @param test Char to test
     * @return true if char is alphabetic
     */
    public static boolean isChar(char test) {
        return ((Character) test).toString().matches("^[A-Za-z]$");
    }

    /**
     * Parses and returns a string of alpha chars
     *
     * @return String that was parsed
     */
    private String _getString() {
        StringBuilder str = new StringBuilder();
        while (!this._isEOF && (
                Lexer.isChar(this._currentChar)
                        || Lexer.isDigit(this._currentChar)
        )) {
            str.append(this._currentChar);
            this.advance();
        }
        return str.toString();
    }
}
