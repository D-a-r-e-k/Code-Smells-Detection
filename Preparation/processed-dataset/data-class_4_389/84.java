/**
     * adds minor, moderate or heavy movement system damage
     * 
     * @param level
     *            a <code>int</code> representing minor damage (1), moderate
     *            damage (2), or heavy damage (3)
     */
public void addMovementDamage(int level) {
    switch(level) {
        case 1:
            if (!minorMovementDamage) {
                minorMovementDamage = true;
                movementDamage += level;
            }
            break;
        case 2:
            if (!moderateMovementDamage) {
                moderateMovementDamage = true;
                movementDamage += level;
            }
            break;
        case 3:
            if (!heavyMovementDamage) {
                heavyMovementDamage = true;
                movementDamage += level;
            }
    }
}
