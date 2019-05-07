/**
     * Prepare the entity for a new round of action.
     */
@Override
public void newRound(int roundNumber) {
    // Perform all base-class behavior.  
    super.newRound(roundNumber);
    // If we're equipped with a Magnetic Mine  
    // launcher, turn it to single shot mode.  
    for (Mounted m : getMisc()) {
        EquipmentType equip = m.getType();
        if (BattleArmor.MINE_LAUNCHER.equals(equip.getInternalName())) {
            m.setMode("Single");
        }
    }
    attacksDuringSwarmResolved = false;
}
