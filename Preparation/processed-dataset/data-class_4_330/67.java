/**
     * Set the <code>Unit</code> value.
     *
     * @param newUnit The new Units value.
     */
public final void setUnit(final Unit newUnit) {
    if (newUnit == null) {
        logger.warning("Unit to add is null");
        return;
    }
    // make sure the owner of the unit is set first, before adding it to the list 
    if (newUnit.getOwner() != null && !this.owns(newUnit)) {
        throw new IllegalStateException(this + " adding another players unit=" + newUnit);
    }
    units.put(newUnit.getId(), newUnit);
}
