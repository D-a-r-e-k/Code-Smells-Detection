/**
     * Add a founding father to the congress.
     *
     * @param father The <code>FoundingFather</code> to add.
     */
public void addFather(FoundingFather father) {
    allFathers.add(father);
    addFeatures(father);
    for (Colony colony : getColonies()) {
        colony.invalidateCache();
    }
}
