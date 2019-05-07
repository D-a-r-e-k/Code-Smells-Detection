/**
     * return if the BA has any kind of active probe
     * @return
     */
public boolean hasActiveProbe() {
    for (Mounted equip : getMisc()) {
        if (equip.getType().hasFlag(MiscType.F_BAP) && !(equip.getType().getInternalName().equals(Sensor.ISIMPROVED) || equip.getType().getInternalName().equals(Sensor.CLIMPROVED))) {
            return true;
        }
    }
    return false;
}
