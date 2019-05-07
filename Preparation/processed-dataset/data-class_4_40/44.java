// modifyName(String,short):String 
/**
     * Converts HTML names string value to constant value. 
     *
     * @see #NAMES_NO_CHANGE
     * @see #NAMES_LOWERCASE
     * @see #NAMES_UPPERCASE
     */
protected static final short getNamesValue(String value) {
    if (value.equals("lower")) {
        return NAMES_LOWERCASE;
    }
    if (value.equals("upper")) {
        return NAMES_UPPERCASE;
    }
    return NAMES_NO_CHANGE;
}
