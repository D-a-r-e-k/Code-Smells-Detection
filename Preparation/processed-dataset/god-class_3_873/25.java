/**
     * Maps the specified <code>key</code> to the specified <code>value</code> in this table.
     * Neither the key nor the
     * value can be <code>null</code>. <p>
     *
     * The value can be retrieved by calling the <code>get</code> method
     * with a key that is equal to the original key.
     *
     * @param      key     the table key.
     * @param      value   the value.
     * @return     the previous value of the specified key in this table,
     *             or <code>null</code> if it did not have one.
     * @exception  NullPointerException  if the key or value is
     *               <code>null</code>.
     * @see     Object#equals(Object)
     * @see     #get(Object)
     */
/** OpenSymphony BEGIN */
public Object put(Object key, Object value) {
    // Call the internal put using persistance  
    return put(key, value, true);
}
