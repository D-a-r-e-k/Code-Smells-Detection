@Override
public boolean hasModularArmor(int loc) {
    for (Mounted mount : this.getEquipment()) {
        if ((mount.getLocation() == loc) && (mount.getType() instanceof MiscType) && ((MiscType) mount.getType()).hasFlag(MiscType.F_MODULAR_ARMOR)) {
            return true;
        }
    }
    return false;
}
