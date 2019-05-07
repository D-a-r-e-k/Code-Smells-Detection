/**
     * Equips a unit for a role.
     *
     * @param unit The <code>Unit</code> to equip if possible.
     * @param role The <code>Role</code> for the unit to take.
     * @param colony The <code>Colony</code> that provides the equipment.
     * @return True if the unit was equipped.
     */
private boolean equipUnit(Unit unit, Role role, Colony colony) {
    if (role == Unit.Role.SOLDIER)
        role = Unit.Role.DRAGOON;
    // Special case 
    List<EquipmentType> equipment = role.getRoleEquipment(spec());
    if (equipment.isEmpty() || !unit.isPerson())
        return false;
    boolean result = false;
    for (EquipmentType et : equipment) {
        if (colony.canProvideEquipment(et) && unit.canBeEquippedWith(et)) {
            unit.setLocation(colony.getTile());
            unit.changeEquipment(et, 1);
            colony.addEquipmentGoods(et, -1);
            result = true;
        }
    }
    return result;
}
