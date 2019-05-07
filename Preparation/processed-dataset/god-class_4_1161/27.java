/**
     * Requires a worker wish for a unit type to be provided to this AIColony.
     * If a suitable wish is already present, the expert and value parameters
     * take precedence as they are more likely to be up to date.
     *
     * @param type The <code>UnitType</code> to wish for.
     * @param expertNeeded Is an expert unit required?
     * @param value The urgency of the wish.
     */
public void requireWorkerWish(UnitType type, boolean expertNeeded, int value) {
    WorkerWish ww = null;
    for (Wish w : wishes) {
        if (w instanceof WorkerWish && ((WorkerWish) w).getUnitType() == type) {
            ww = (WorkerWish) w;
            break;
        }
    }
    if (ww != null) {
        ww.update(type, expertNeeded, value);
    } else {
        ww = new WorkerWish(getAIMain(), colony, value, type, expertNeeded);
        wishes.add(ww);
        logger.finest(colony.getName() + " makes new worker wish: " + ww);
    }
}
