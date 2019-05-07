/**
     * Gets a nation name suitable for use in message IDs.
     *
     * @return a <code>String</code> value
     */
public String getNationNameKey() {
    return nationID.substring(nationID.lastIndexOf('.') + 1).toUpperCase(Locale.US);
}
