/**
     * Return this Player's nation.
     *
     * @return a <code>String</code> value
     */
public Nation getNation() {
    return getSpecification().getNation(nationID);
}
