@Override
public PilotingRollData addEntityBonuses(PilotingRollData prd) {
    if (movementDamage > 0) {
        prd.addModifier(movementDamage, "Steering Damage");
    }
    if (commanderHit) {
        prd.addModifier(1, "commander injured");
    }
    if (driverHit) {
        prd.addModifier(2, "driver injured");
    }
    // are we wheeled and in light snow?  
    IHex hex = game.getBoard().getHex(getPosition());
    if ((null != hex) && (getMovementMode() == EntityMovementMode.WHEELED) && (hex.terrainLevel(Terrains.SNOW) == 1)) {
        prd.addModifier(1, "thin snow");
    }
    // VDNI bonus?  
    if (getCrew().getOptions().booleanOption("vdni") && !getCrew().getOptions().booleanOption("bvdni")) {
        prd.addModifier(-1, "VDNI");
    }
    if (hasModularArmor()) {
        prd.addModifier(1, "Modular Armor");
    }
    return prd;
}
