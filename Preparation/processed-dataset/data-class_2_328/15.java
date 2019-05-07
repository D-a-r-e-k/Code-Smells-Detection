/**
     * Return all types which have none of the given abilities.
     *
     * @param abilities The abilities for the search
     * @return a <code>List</code> of <code>UnitType</code>
     */
public <T extends FreeColGameObjectType> List<T> getTypesWithoutAbility(Class<T> resultType, String... abilities) {
    ArrayList<T> result = new ArrayList<T>();
    type: for (FreeColGameObjectType type : allTypes.values()) {
        if (resultType.isInstance(type)) {
            for (String ability : abilities) {
                if (type.hasAbility(ability))
                    continue type;
            }
            result.add(resultType.cast(type));
        }
    }
    return result;
}
