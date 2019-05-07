/**
     * Determines whether this player has a certain Founding father.
     *
     * @param someFather a <code>FoundingFather</code> value
     * @return Whether this player has this Founding father
     * @see FoundingFather
     */
public boolean hasFather(FoundingFather someFather) {
    return allFathers.contains(someFather);
}
