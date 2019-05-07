/**
     * Is option with this ID present?  This is helpful when options are
     * optionally(!) present, for example model.option.priceIncrease.artillery
     * exists but model.option.priceIncrease.frigate does not.
     *
     * @param Id a <code>String</code> value
     * @return True/false on presence of option Id
     */
public boolean hasOption(String Id) {
    return Id != null && allOptions.containsKey(Id);
}
