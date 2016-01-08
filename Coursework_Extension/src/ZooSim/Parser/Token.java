package ZooSim.Parser;

/**
 * Stores a token type/value
 *
 * @author Huw Jones
 * @since 23/10/2015
 */
public class Token {
    private final String _type;
    private final Object _value;

    public Token(String type, Object value) {
        this._type = type;
        this._value = value;
    }

    /**
     * Compares a token to this token
     *
     * @param compare Token to compare
     * @return True if tokens match
     */
    public boolean equals(Token compare) {
        return (compare.getType().equals(_type) && compare.getValue().equals(_value));
    }

    /**
     * Gets the type of token
     *
     * @return Token type
     */
    public String getType() {
        return _type;
    }

    /**
     * Gets the value of the token
     *
     * @return Token value
     */
    public Object getValue() {
        return _value;
    }

    /**
     * Compares this tokens type to another type
     *
     * @param type Type to compare
     * @return True if types match
     */
    public boolean isType(String type) {
        return this._type.equals(type);
    }


    @Override
    public String toString() {
        return "Token({" + _type + "}, {" + _value.toString() + "})";
    }


}
