/**
     * Returns the <code>StringOption</code> with the given ID. Throws an
     * IllegalArgumentException if the ID is null, or if no such Type can be
     * retrieved.
     *
     * @param Id a <code>String</code> value
     * @return an <code>StringOption</code> value
     */
public StringOption getStringOption(String Id) {
    return (StringOption) getOption(Id);
}
