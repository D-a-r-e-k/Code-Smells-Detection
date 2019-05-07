/**
     * Return true if the field is a numeric field,
     * like Integer, Short etc..
     *
     * @return true if numeric
     */
public boolean isNumeric() {
    return ("java.lang.Integer".equals(getType())) || ("java.lang.Byte".equals(getType())) || ("java.lang.Long".equals(getType())) || ("java.lang.Short".equals(getType()));
}
