/**
     * Gets a settlement name suitable for this player.
     *
     * @param random An optional pseudo-random number source.
     * @return A new settlement name.
     */
public String getSettlementName(Random random) {
    Game game = getGame();
    if (settlementNames == null)
        initializeSettlementNames(random);
    // Try the names in the players national name list. 
    while (!settlementNames.isEmpty()) {
        String name = settlementNames.remove(0);
        if (game.getSettlement(name) == null)
            return name;
    }
    // Fallback method 
    final String base = Messages.message((isEuropean()) ? "Colony" : "Settlement") + "-";
    String name;
    int i = settlements.size() + 1;
    while (game.getSettlement(name = base + Integer.toString(i)) != null) {
        i++;
    }
    return name;
}
