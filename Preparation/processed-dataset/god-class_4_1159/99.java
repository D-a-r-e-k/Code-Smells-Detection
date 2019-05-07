/**
     * Sets the set of offered fathers.
     *
     * @param fathers A list of <code>FoundingFather</code>s to offer.
     */
public void setOfferedFathers(List<FoundingFather> fathers) {
    clearOfferedFathers();
    offeredFathers.addAll(fathers);
}
