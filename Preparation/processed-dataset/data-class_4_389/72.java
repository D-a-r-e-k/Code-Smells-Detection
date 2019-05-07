@Override
public double getCost(boolean ignoreAmmo) {
    double cost = 0;
    cost += getEngine().getBaseCost() * getEngine().getRating() * weight / 75.0;
    double controlWeight = Math.ceil(weight * 0.05 * 2.0) / 2.0;
    // ?  
    // should  
    // be  
    // rounded  
    // up to  
    // nearest  
    // half-ton  
    cost += 10000 * controlWeight;
    cost += weight / 10.0 * 10000;
    // IS has no variations, no Endo etc.  
    double freeHeatSinks = engine.getWeightFreeEngineHeatSinks();
    int sinks = 0;
    double turretWeight = 0;
    double paWeight = 0;
    for (Mounted m : getWeaponList()) {
        WeaponType wt = (WeaponType) m.getType();
        if (wt.hasFlag(WeaponType.F_LASER) || wt.hasFlag(WeaponType.F_PPC)) {
            sinks += wt.getHeat();
            paWeight += wt.getTonnage(this) / 10.0;
        }
        if (!hasNoTurret() && (m.getLocation() == Tank.LOC_TURRET)) {
            turretWeight += wt.getTonnage(this) / 10.0;
        }
    }
    paWeight = Math.ceil(paWeight * 10.0) / 10;
    if (engine.isFusion()) {
        paWeight = 0;
    }
    turretWeight = Math.ceil(turretWeight * 2) / 2;
    cost += 20000 * paWeight;
    cost += 2000 * Math.max(0, sinks - freeHeatSinks);
    cost += turretWeight * 5000;
    cost += getArmorWeight() * EquipmentType.getArmorCost(armorType);
    // armor  
    double diveTonnage;
    switch(movementMode) {
        case HOVER:
        case HYDROFOIL:
        case VTOL:
        case SUBMARINE:
            diveTonnage = weight / 10.0;
            break;
        default:
            diveTonnage = 0.0;
            break;
    }
    if (movementMode != EntityMovementMode.VTOL) {
        cost += diveTonnage * 20000;
    } else {
        cost += diveTonnage * 40000;
    }
    cost += getWeaponsAndEquipmentCost(ignoreAmmo);
    double multiplier = 1.0;
    switch(movementMode) {
        case HOVER:
        case SUBMARINE:
            multiplier += weight / 50.0;
            break;
        case HYDROFOIL:
            multiplier += weight / 75.0;
            break;
        case NAVAL:
        case WHEELED:
            multiplier += weight / 200.0;
            break;
        case TRACKED:
            multiplier += weight / 100.0;
            break;
        case VTOL:
            multiplier += weight / 30.0;
            break;
    }
    if (hasWorkingMisc(MiscType.F_FLOTATION_HULL) || hasWorkingMisc(MiscType.F_VACUUM_PROTECTION) || hasWorkingMisc(MiscType.F_ENVIRONMENTAL_SEALING)) {
        cost *= 1.25;
    }
    return Math.round(cost * multiplier);
}
