@Override
public boolean doomedInVacuum() {
    for (Mounted m : getEquipment()) {
        if ((m.getType() instanceof MiscType) && m.getType().hasFlag(MiscType.F_VACUUM_PROTECTION)) {
            return false;
        }
        if ((m.getType() instanceof MiscType) && m.getType().hasFlag(MiscType.F_ENVIRONMENTAL_SEALING)) {
            return false;
        }
    }
    return true;
}
