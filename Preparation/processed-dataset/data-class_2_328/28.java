/**
     * Gets the string value of an option.
     *
     * @param id The id of the option.
     * @return The value.
     * @exception IllegalArgumentException If there is no string
     *     value associated with the specified option.
     * @exception NullPointerException if the given
     *     <code>Option</code> does not exist.
     */
public String getString(String id) {
    try {
        return ((StringOption) getOption(id)).getValue();
    } catch (ClassCastException e) {
        throw new IllegalArgumentException("No string value associated with the specified option.");
    }
}
