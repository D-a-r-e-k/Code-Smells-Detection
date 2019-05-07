/**
     * Gets the name of this players capital.  Only meaningful to natives.
     *
     * @param random An optional pseudo-random number source.
     * @return The name of this players capital.
     */
public String getCapitalName(Random random) {
    if (isEuropean())
        return null;
    if (capitalName == null)
        initializeSettlementNames(random);
    return capitalName;
}
