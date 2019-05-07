// End public TargetRoll getStealthModifier( char )  
@Override
public double getCost(boolean ignoreAmmo) {
    // TODO: do this correctly  
    // Hopefully the cost is correctly set.  
    if (myCost > 0) {
        return myCost;
    }
    // If it's not, I guess we default to the book values...  
    if (chassis.equals("Clan Elemental")) {
        return 3500000;
    }
    if (chassis.equals("Clan Gnome")) {
        return 5250000;
    }
    if (chassis.equals("Clan Salamander")) {
        return 3325000;
    }
    if (chassis.equals("Clan Sylph")) {
        return 3325000;
    }
    if (chassis.equals("Clan Undine")) {
        return 3500000;
    }
    if (chassis.equals("IS Standard")) {
        return 2400000;
    }
    if (chassis.equals("Achileus")) {
        return 1920000;
    }
    if (chassis.equals("Cavalier")) {
        return 2400000;
    }
    if (chassis.equals("Fa Shih")) {
        return 2250000;
    }
    if (chassis.equals("Fenrir")) {
        return 2250000;
    }
    if (chassis.equals("Gray Death Light Scout")) {
        return 1650000;
    }
    if (chassis.equals("Gray Death Standard")) {
        return 2400000;
    }
    if (chassis.equals("Infiltrator")) {
        if (model.equals("Mk I")) {
            return 1800000;
        }
        return 2400000;
    }
    if (chassis.equals("Kage")) {
        return 1850000;
    }
    if (chassis.equals("Kanazuchi")) {
        return 3300000;
    }
    if (chassis.equals("Longinus")) {
        return 2550000;
    }
    if (chassis.equals("Purifier")) {
        return 2400000;
    }
    if (chassis.equals("Raiden")) {
        return 2400000;
    }
    if (chassis.equals("Sloth")) {
        return 1800000;
    }
    return 0;
}
