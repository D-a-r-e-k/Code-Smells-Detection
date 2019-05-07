/**
     * Return the unit types which have none of the given abilities
     *
     * @param abilities The abilities for the search
     * @return a <code>List</code> of <code>UnitType</code>
     */
public List<UnitType> getUnitTypesWithoutAbility(String... abilities) {
    return getTypesWithoutAbility(UnitType.class, abilities);
}
