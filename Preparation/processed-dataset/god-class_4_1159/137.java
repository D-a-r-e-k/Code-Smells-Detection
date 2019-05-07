/**
     * Returns the nation of this player as a String.
     *
     * @return The nation of this player as a String.
     */
public StringTemplate getNationName() {
    return (playerType == PlayerType.REBEL || playerType == PlayerType.INDEPENDENT) ? StringTemplate.name(independentNationName) : StringTemplate.key(nationID + ".name");
}
