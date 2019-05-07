/**
     * Updates the amount of immigration needed to emigrate a <code>Unit</code>
     * from <code>Europe</code>.
     */
public void updateImmigrationRequired() {
    if (!canRecruitUnits())
        return;
    final Specification spec = getSpecification();
    int base = spec.getInteger("model.option.crossesIncrement");
    immigrationRequired += (int) applyModifier(base, "model.modifier.religiousUnrestBonus");
}
