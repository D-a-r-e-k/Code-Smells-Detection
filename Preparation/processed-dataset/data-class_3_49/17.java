/**
     * Tests if the specified object is a key in this table.
     *
     * @param   key   possible key.
     * @return  <code>true</code> if and only if the specified object
     *          is a key in this table, as determined by the
     *          <tt>equals</tt> method; <code>false</code> otherwise.
     * @exception  NullPointerException  if the key is
     *               <code>null</code>.
     * @see     #contains(Object)
     */
public boolean containsKey(Object key) {
    return get(key) != null;
}
