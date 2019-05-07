/**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#load(
     *      org.exolab.castor.persist.spi.Identity, org.castor.persist.ProposedEntity,
     *      org.exolab.castor.mapping.AccessMode)
     */
public final synchronized Object load(final Identity identity, final ProposedEntity proposedObject, final AccessMode suggestedAccessMode) throws PersistenceException {
    return load(identity, proposedObject, suggestedAccessMode, null);
}
