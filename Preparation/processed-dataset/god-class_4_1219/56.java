public final String getBLK() {
    String newline = "\r\n";
    StringBuffer buff = new StringBuffer();
    buff.append("<BlockVersion>");
    buff.append(newline);
    buff.append("1");
    buff.append(newline);
    buff.append("</BlockVersion>");
    buff.append(newline);
    buff.append("<UnitType>");
    buff.append(newline);
    buff.append("BattleArmor");
    buff.append(newline);
    buff.append("</UnitType>");
    buff.append(newline);
    buff.append("<name>");
    buff.append(newline);
    buff.append(getChassis());
    buff.append(newline);
    buff.append("</name>");
    buff.append(newline);
    buff.append("<model>");
    buff.append(newline);
    buff.append(getModel());
    buff.append(newline);
    buff.append("</model>");
    buff.append(newline);
    buff.append("<year>");
    buff.append(newline);
    buff.append(getYear());
    buff.append(newline);
    buff.append("</year>");
    buff.append(newline);
    buff.append("<type>");
    buff.append(newline);
    switch(getTechLevel()) {
        case TechConstants.T_INTRO_BOXSET:
            buff.append("IS Level 1");
            break;
        case TechConstants.T_IS_TW_NON_BOX:
            buff.append("IS Level 2");
            break;
        case TechConstants.T_IS_ADVANCED:
            buff.append("IS Level 3");
            break;
        case TechConstants.T_IS_EXPERIMENTAL:
            buff.append("IS Level 4");
            break;
        case TechConstants.T_IS_UNOFFICIAL:
            buff.append("IS Level 5");
            break;
        case TechConstants.T_CLAN_TW:
            buff.append("Clan Level 2");
            break;
        case TechConstants.T_CLAN_ADVANCED:
            buff.append("Clan Level 3");
            break;
        case TechConstants.T_CLAN_EXPERIMENTAL:
            buff.append("Clan Level 4");
            break;
        case TechConstants.T_CLAN_UNOFFICIAL:
            buff.append("Clan Level 5");
            break;
    }
    buff.append(newline);
    buff.append("</type>");
    buff.append(newline);
    buff.append("<trooper count>");
    buff.append(newline);
    buff.append(getWeight());
    buff.append(newline);
    buff.append("</trooper count>");
    buff.append(newline);
    buff.append("<weightclass>");
    buff.append(newline);
    switch(getWeightClass()) {
        case EntityWeightClass.WEIGHT_BA_PAL:
            buff.append("0");
            break;
        case EntityWeightClass.WEIGHT_BA_LIGHT:
            buff.append("1");
            break;
        case EntityWeightClass.WEIGHT_BA_MEDIUM:
            buff.append("2");
            break;
        case EntityWeightClass.WEIGHT_BA_HEAVY:
            buff.append("3");
            break;
        case EntityWeightClass.WEIGHT_BA_ASSAULT:
            buff.append("4");
            break;
    }
    buff.append(newline);
    buff.append("</weightclass>");
    buff.append(newline);
    buff.append("<motion_type>");
    buff.append(newline);
    switch(getMovementMode()) {
        case INF_JUMP:
            buff.append("jump");
            break;
        case INF_LEG:
            buff.append("leg");
            break;
        case VTOL:
            buff.append("vtol");
            break;
        case INF_UMU:
            buff.append("submarine");
            break;
    }
    buff.append(newline);
    buff.append("</motion_type>");
    buff.append(newline);
    buff.append("<chassis>");
    buff.append(newline);
    switch(getChassisType()) {
        case BattleArmor.CHASSIS_TYPE_BIPED:
            buff.append("biped");
            break;
        case BattleArmor.CHASSIS_TYPE_QUAD:
            buff.append("quad");
            break;
    }
    buff.append("</chassis>");
    buff.append(newline);
    buff.append("<cruiseMP>");
    buff.append(newline);
    buff.append(getOriginalRunMP());
    buff.append(newline);
    buff.append("</cruiseMP>");
    buff.append(newline);
    buff.append("<jumpMP>");
    buff.append(newline);
    buff.append(getOriginalJumpMP());
    buff.append(newline);
    buff.append("</jumpMP>");
    buff.append(newline);
    buff.append("<armor>");
    buff.append(newline);
    buff.append(getOArmor(LOC_TROOPER_1));
    buff.append(newline);
    buff.append("</armor>");
    buff.append(newline);
    for (int i = 0; i < locations(); i++) {
        boolean found = false;
        for (Mounted m : getEquipment()) {
            // don't write out swarm and leg attack, those get added dynamically  
            if ((m.getType() instanceof WeaponType) && m.getType().hasFlag(WeaponType.F_INFANTRY_ATTACK)) {
                continue;
            }
            if (m.getLocation() == i) {
                if (!found) {
                    found = true;
                    buff.append("<");
                    buff.append(getLocationName(i));
                    buff.append(" equipment>");
                    buff.append(newline);
                }
                buff.append(m.getType().getInternalName());
                buff.append(newline);
            }
        }
        if (found) {
            buff.append("</");
            buff.append(getLocationName(i));
            buff.append(" equipment>");
            buff.append(newline);
        }
    }
    return buff.toString();
}
