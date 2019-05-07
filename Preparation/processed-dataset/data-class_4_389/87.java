@Override
protected void addEquipment(Mounted mounted, int loc, boolean rearMounted) throws LocationFullException {
    super.addEquipment(mounted, loc, rearMounted);
    // Add the piece equipment to our slots.  
    addCritical(loc, new CriticalSlot(CriticalSlot.TYPE_EQUIPMENT, getEquipmentNum(mounted), true, mounted));
}
