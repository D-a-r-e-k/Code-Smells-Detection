/**
     * Registers an Ability's id as defined. This is useful for
     * abilities that are required rather than provided by
     * FreeColGameObjectTypes.
     *
     * @param id a <code>String</code> value
     */
public void addAbility(String id) {
    if (!allAbilities.containsKey(id)) {
        allAbilities.put(id, new ArrayList<Ability>());
    }
}
