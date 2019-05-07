/**
     * Gets this AI object's identifier.
     *
     * @return The id of the colony.
     */
@Override
public String getId() {
    if (colony == null) {
        logger.warning("Uninitialized AI colony");
        return null;
    }
    return colony.getId();
}
