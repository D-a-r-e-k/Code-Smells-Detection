/**
     * Gets the port closest to Europe owned by this player.
     *
     * @return This players closest port.
     */
public Settlement getClosestPortForEurope() {
    int bestValue = INFINITY;
    Settlement best = null;
    for (Settlement settlement : getSettlements()) {
        int value = settlement.getHighSeasCount();
        if (bestValue > value) {
            bestValue = value;
            best = settlement;
        }
    }
    return best;
}
