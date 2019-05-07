/**
     * can this BattleArmor ride as Mechanized BA?
     * @return
     */
public boolean canDoMechanizedBA() {
    if (getChassisType() != CHASSIS_TYPE_QUAD) {
        int tBasicManipulatorCount = countWorkingMisc(MiscType.F_BASIC_MANIPULATOR);
        int tArmoredGloveCount = countWorkingMisc(MiscType.F_ARMORED_GLOVE);
        int tBattleClawCount = countWorkingMisc(MiscType.F_BATTLE_CLAW);
        switch(getWeightClass()) {
            case EntityWeightClass.WEIGHT_BA_PAL:
            case EntityWeightClass.WEIGHT_BA_LIGHT:
                if ((tArmoredGloveCount > 1) || (tBasicManipulatorCount > 0) || (tBattleClawCount > 0)) {
                    return true;
                }
                break;
            case EntityWeightClass.WEIGHT_BA_MEDIUM:
                if ((tBasicManipulatorCount > 0) || (tBattleClawCount > 0)) {
                    return true;
                }
                break;
            case EntityWeightClass.WEIGHT_BA_HEAVY:
                if ((tBasicManipulatorCount > 0) || (tBattleClawCount > 0)) {
                    return true;
                }
                break;
            case EntityWeightClass.WEIGHT_BA_ASSAULT:
            default:
                return false;
        }
    }
    return false;
}
