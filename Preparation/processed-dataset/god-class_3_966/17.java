/**
     * Determines if the {@link java.sql.ResultSet} generated
     * from this object will contain distinct tuples (default is false).
     * @param distinct true for distinct tuples
     */
public void setDistinct(boolean distinct) {
    if (_resolved) {
        throw new IllegalStateException("Already resolved.");
    }
    _distinct = distinct;
}
