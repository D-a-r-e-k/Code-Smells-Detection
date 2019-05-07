/**
     * Returns the <code>RangeOption</code> with the given ID. Throws an
     * IllegalArgumentException if the ID is null, or if no such Type can be
     * retrieved.
     *
     * @param Id a <code>String</code> value
     * @return an <code>RangeOption</code> value
     */
public RangeOption getRangeOption(String Id) {
    return (RangeOption) getOption(Id);
}
