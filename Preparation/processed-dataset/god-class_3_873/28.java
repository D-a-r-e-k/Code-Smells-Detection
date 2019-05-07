/**
     * Like <code>remove(Object)</code>, but ensures that the entry will be removed from the persistent store, too,
     * even if overflowPersistence or unlimitedDiskcache are true.
     *
     * @param   key   the key that needs to be removed.
     * @return  the value to which the key had been mapped in this table,
     *          or <code>null</code> if the key did not have a mapping.
     */
public Object removeForce(Object key) {
    return remove(key, true, true);
}
