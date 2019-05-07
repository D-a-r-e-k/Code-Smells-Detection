/**
     * Return the unit types which have any of the given abilities
     *
     * @param abilities The abilities for the search
     * @return a <code>List</code> of <code>UnitType</code>
     */
public List<UnitType> getUnitTypesWithAbility(String... abilities) {
    return getTypesWithAbility(UnitType.class, abilities);
}
