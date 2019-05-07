/** 
     * @return Whether the String passed is an alias for a standard formatter 
     */
public boolean isKnownType(String t) {
    return types.containsKey(t);
}
