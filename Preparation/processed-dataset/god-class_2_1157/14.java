/**
     * Return all types which have any of the given abilities.
     *
     * @param abilities The abilities for the search
     * @return a <code>List</code> of <code>UnitType</code>
     */
public <T extends FreeColGameObjectType> List<T> getTypesWithAbility(Class<T> resultType, String... abilities) {
    ArrayList<T> result = new ArrayList<T>();
    for (FreeColGameObjectType type : allTypes.values()) {
        if (resultType.isInstance(type)) {
            for (String ability : abilities) {
                if (type.hasAbility(ability)) {
                    result.add(resultType.cast(type));
                    break;
                }
            }
        }
    }
    return result;
}
