@Override
public boolean hasArmoredEngine() {
    for (int slot = 0; slot < getNumberOfCriticals(LOC_BODY); slot++) {
        CriticalSlot cs = getCritical(LOC_BODY, slot);
        if ((cs != null) && (cs.getType() == CriticalSlot.TYPE_SYSTEM) && (cs.getIndex() == Mech.SYSTEM_ENGINE)) {
            return cs.isArmored();
        }
    }
    return false;
}
