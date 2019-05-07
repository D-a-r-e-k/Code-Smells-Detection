/**
     * Returns the <code>OptionGroup</code> with the given ID. Throws an
     * IllegalArgumentException if the ID is null or unknown.
     *
     * @param Id a <code>String</code> value
     * @return an <code>OptionGroup</code> value
     */
public OptionGroup getOptionGroup(String Id) throws IllegalArgumentException {
    if (Id == null) {
        throw new IllegalArgumentException("Trying to retrieve OptionGroup" + " with ID 'null'.");
    } else if (!allOptionGroups.containsKey(Id)) {
        throw new IllegalArgumentException("Trying to retrieve OptionGroup" + " with ID '" + Id + "' returned 'null'.");
    } else {
        return allOptionGroups.get(Id);
    }
}
