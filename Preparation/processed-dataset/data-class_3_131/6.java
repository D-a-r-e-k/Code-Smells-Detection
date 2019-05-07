/**
     * Sets the root {@link FromNode} for the select statement.
     */
public void setFrom(FromNode from) {
    if (_resolved) {
        throw new IllegalStateException("Already resolved.");
    }
    _from = from;
}
