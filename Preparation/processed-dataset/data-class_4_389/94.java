@Override
public boolean hasModularArmor() {
    for (Mounted mount : this.getEquipment()) {
        if (!mount.isDestroyed() && (mount.getType() instanceof MiscType) && ((MiscType) mount.getType()).hasFlag(MiscType.F_MODULAR_ARMOR)) {
            return true;
        }
    }
    return false;
}
