/**
     * Update the unit to reflect damages taken in this phase.
     */
@Override
public void applyDamage() {
    super.applyDamage();
    int troopersAlive = 0;
    for (int i = 0; i < locations(); i++) {
        if (getInternal(i) > 0) {
            troopersAlive++;
        }
    }
    troopersShooting = troopersAlive;
}
