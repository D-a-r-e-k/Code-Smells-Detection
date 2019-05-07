/**
     * Returns the <code>AbstractOption</code> with the given ID. Throws an
     * IllegalArgumentException if the ID is null or unknown.
     *
     * @param Id a <code>String</code> value
     * @return an <code>AbstractOption</code> value
     */
public AbstractOption getOption(String Id) throws IllegalArgumentException {
    if (Id == null) {
        throw new IllegalArgumentException("Trying to retrieve AbstractOption" + " with ID 'null'.");
    } else if (!allOptions.containsKey(Id)) {
        throw new IllegalArgumentException("Trying to retrieve AbstractOption" + " with ID '" + Id + "' returned 'null'.");
    } else {
        return allOptions.get(Id);
    }
}
