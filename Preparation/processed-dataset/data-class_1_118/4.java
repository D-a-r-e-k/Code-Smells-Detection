/**
     * Wrap result to a Set.
     *
     * @return null if we overflowed! the set otherwise
     */
public Set<E> getNextSet() {
    List<E> result = getNextArray();
    if (result == null) {
        return null;
    } else // wrap in a SET 
    {
        Set<E> resultSet = new LinkedHashSet<E>(result);
        return resultSet;
    }
}
