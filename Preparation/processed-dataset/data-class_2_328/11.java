/**
     * Returns the <code>FreeColGameObjectType</code> with the given
     * ID.  Throws an IllegalArgumentException if the ID is
     * null. Throws and IllegalArgumentException if no such Type
     * can be retrieved and initialization is complete.
     *
     * @param Id a <code>String</code> value
     * @param type a <code>Class</code> value
     * @return a <code>FreeColGameObjectType</code> value
     * @exception IllegalArgumentException if an error occurs
     */
public <T extends FreeColGameObjectType> T getType(String Id, Class<T> type) throws IllegalArgumentException {
    if (Id == null) {
        throw new IllegalArgumentException("Trying to retrieve FreeColGameObjectType" + " with ID 'null'.");
    } else if (allTypes.containsKey(Id)) {
        try {
            return type.cast(allTypes.get(Id));
        } catch (ClassCastException cce) {
            logger.warning(Id + " caused ClassCastException!");
            throw (cce);
        }
    } else if (allTypes.containsKey(mangle(Id))) {
        // @compat 0.9.x 
        return type.cast(allTypes.get(mangle(Id)));
    } else if (initialized) {
        throw new IllegalArgumentException("Undefined FreeColGameObjectType" + " with ID '" + Id + "'.");
    } else {
        // forward declaration of new type 
        try {
            Constructor<T> c = type.getConstructor(String.class, Specification.class);
            T result = c.newInstance(Id, this);
            allTypes.put(Id, result);
            return result;
        } catch (Exception e) {
            logger.warning(e.toString());
            return null;
        }
    }
}
