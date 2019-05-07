/**
     * Gets the number of liberty points needed to recruit the next
     * founding father.
     *
     * @return How many more liberty points the <code>Player</code>
     *         needs in order to recruit the next founding father.
     * @see #incrementLiberty
     */
public int getRemainingFoundingFatherCost() {
    return getTotalFoundingFatherCost() - getLiberty();
}
