/**
     * Describe <code>getEuropeName</code> method here.
     *
     * @return a <code>String</code> value
     */
public String getEuropeNameKey() {
    if (europe == null) {
        return null;
    } else {
        return nationID + ".europe";
    }
}
