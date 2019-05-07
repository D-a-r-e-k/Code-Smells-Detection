/**
     * Something bad happened, there is no remaining unit working in
     * the colony.
     *
     * Throwing an exception stalls the AI and wrecks the colony in a
     * weird way.  Try to recover by hopefully finding a unit outside
     * the colony and stuffing it into the town hall.
     */
private void avertAutoDestruction() {
    String msg = "Colony " + colony.getName() + " rearrangement leaves no units, " + colony.getTile().getUnitCount() + " available";
    for (Unit u : colony.getTile().getUnitList()) {
        msg += ", " + u.toString();
    }
    List<GoodsType> libertyGoods = getSpecification().getLibertyGoodsTypeList();
    for (Unit u : colony.getTile().getUnitList()) {
        if (!u.isPerson())
            continue;
        for (WorkLocation wl : colony.getAvailableWorkLocations()) {
            if (!wl.canAdd(u))
                continue;
            for (GoodsType type : libertyGoods) {
                if (wl.getPotentialProduction(type, u.getType()) > 0 && AIMessage.askWork(getAIUnit(u), wl) && u.getLocation() == wl) {
                    AIMessage.askChangeWorkType(getAIUnit(u), type);
                    msg += ".  Autodestruct averted with " + u + ".";
                    logger.warning(msg);
                    break;
                }
            }
        }
    }
    // No good, no choice but to fail. 
    if (colony.getWorkLocationUnitCount() <= 0) {
        throw new IllegalStateException(msg);
    }
}
