/**
     * Return a list of FreeColGameObjectTypes that provide the required ability.
     *
     * @param id the ability id
     * @param value the ability value
     * @return a list of FreeColGameObjectTypes that provide the required ability.
     */
public List<FreeColGameObjectType> getTypesProviding(String id, boolean value) {
    List<FreeColGameObjectType> result = new ArrayList<FreeColGameObjectType>();
    for (Ability ability : getAbilities(id)) {
        if (ability.getValue() == value && ability.getSource() instanceof FreeColGameObjectType) {
            result.add((FreeColGameObjectType) ability.getSource());
        }
    }
    return result;
}
