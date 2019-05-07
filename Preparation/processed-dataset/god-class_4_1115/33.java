/**
     * Returns the result of executing this Select.
     *
     * @param maxrows may be 0 to indicate no limit on the number of rows.
     * Positive values limit the size of the result set.
     * @return the result of executing this Select
     */
Result getResult(Session session, int maxrows) {
    Result r = getSingleResult(session, maxrows);
    r.getNavigator().reset();
    return r;
}
