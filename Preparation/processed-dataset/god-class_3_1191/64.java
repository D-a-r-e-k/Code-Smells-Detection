/**
     * {@inheritDoc}
     */
public final NamedNativeQuery getNamedNativeQuery(final ClassMolder molder, final String name) throws QueryException {
    if (molder == null) {
        throw new QueryException("Invalid argument - molder is null");
    }
    return molder.getNamedNativeQuery(name);
}
