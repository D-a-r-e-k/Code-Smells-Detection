/**
     * Returns <tt>true</tt> if this map maps one or more keys to the
     * specified value. Note: This method requires a full internal
     * traversal of the hash table, and so is much slower than
     * method <tt>containsKey</tt>.
     *
     * @param value value whose presence in this map is to be tested.
     * @return <tt>true</tt> if this map maps one or more keys to the
     * specified value.
     * @exception  NullPointerException  if the value is <code>null</code>.
     */
public boolean containsValue(Object value) {
    if (value == null) {
        throw new NullPointerException();
    }
    Entry[] tab = getTableForReading();
    for (int i = 0; i < tab.length; ++i) {
        for (Entry e = tab[i]; e != null; e = e.next) {
            Object v = e.value;
            if ((v != null) && value.equals(v)) {
                return true;
            }
        }
    }
    return false;
}
