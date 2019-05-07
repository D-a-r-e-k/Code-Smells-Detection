/**
     * {@inheritDoc}
     */
public final String getNamedQuery(final ClassMolder molder, final String name) throws QueryException {
    if (molder == null) {
        throw new QueryException("Invalid argument - molder is null");
    }
    return molder.getNamedQuery(name);
}
