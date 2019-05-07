/**
     * Returns the <code>IntegerOption</code> with the given ID. Throws an
     * IllegalArgumentException if the ID is null, or if no such Type can be
     * retrieved.
     *
     * @param Id a <code>String</code> value
     * @return an <code>IntegerOption</code> value
     */
public IntegerOption getIntegerOption(String Id) {
    return (IntegerOption) getOption(Id);
}
