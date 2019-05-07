/**
     * Find a <code>Settlement</code> by name.
     *
     * @param name The name of the <code>Settlement</code>.
     * @return The <code>Settlement</code>, or <code>null</code> if not found.
     **/
public Settlement getSettlement(String name) {
    return (isIndian()) ? getIndianSettlement(name) : getColony(name);
}
