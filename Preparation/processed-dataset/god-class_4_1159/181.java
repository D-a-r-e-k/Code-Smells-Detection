/**
     * Recalculate bells bonus when tax changes.
     *
     * @return True if a bells bonus was set.
     */
protected boolean recalculateBellsBonus() {
    Set<Modifier> libertyBonus = getModifierSet("model.goods.bells");
    boolean ret = false;
    for (Ability ability : getAbilitySet("model.ability.addTaxToBells")) {
        FreeColObject source = ability.getSource();
        if (source != null) {
            for (Modifier modifier : libertyBonus) {
                if (source.equals(modifier.getSource())) {
                    modifier.setValue(tax);
                    ret = true;
                }
            }
        }
    }
    return ret;
}
