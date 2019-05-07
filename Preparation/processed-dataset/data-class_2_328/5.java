/**
     * Registers an Ability as defined.
     *
     * @param ability an <code>Ability</code> value
     */
public void addAbility(Ability ability) {
    String id = ability.getId();
    addAbility(id);
    allAbilities.get(id).add(ability);
}
