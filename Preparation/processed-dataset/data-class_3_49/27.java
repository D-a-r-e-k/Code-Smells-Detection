/**
     * Removes the key (and its corresponding value) from this table.
     * This method does nothing if the key is not in the table.
     *
     * @param   key   the key that needs to be removed.
     * @return  the value to which the key had been mapped in this table,
     *          or <code>null</code> if the key did not have a mapping.
     */
/** OpenSymphony BEGIN */
public Object remove(Object key) {
    return remove(key, true, false);
}
