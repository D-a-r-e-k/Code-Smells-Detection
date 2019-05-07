/**
     * Standardized log of an instance of cheating by this player.
     *
     * @param what A description of the cheating.
     */
public void logCheat(String what) {
    logger.finest("CHEAT: " + getGame().getTurn().getNumber() + " " + Utils.lastPart(getNationID(), ".") + " " + what);
}
