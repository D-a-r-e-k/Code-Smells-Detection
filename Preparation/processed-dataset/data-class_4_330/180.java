/**
     * Sets the current tax
     *
     * @param amount The new tax.
     */
public void setTax(int amount) {
    tax = amount;
    if (recalculateBellsBonus()) {
        for (Colony colony : getColonies()) {
            colony.invalidateCache();
        }
    }
}
