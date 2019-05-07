/**
     * Returns an enumeration of the keys in this table.
     *
     * @return  an enumeration of the keys in this table.
     * @see     Enumeration
     * @see     #elements()
     * @see        #keySet()
     * @see        Map
     */
public Enumeration keys() {
    return new KeyIterator();
}
