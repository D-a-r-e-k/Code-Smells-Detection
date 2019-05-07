/**
     * return if this BA has improved sensors
     * @return
     */
public boolean hasImprovedSensors() {
    for (Mounted equip : getMisc()) {
        if (equip.getType().hasFlag(MiscType.F_BAP)) {
            if (equip.getType().getInternalName().equals(Sensor.ISIMPROVED) || equip.getType().getInternalName().equals(Sensor.CLIMPROVED)) {
                return true;
            }
        }
    }
    return false;
}
