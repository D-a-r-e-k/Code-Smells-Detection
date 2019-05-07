/**
     * Gets a list of the players in rebellion against this (REF) player.
     *
     * @return A list of nations in rebellion against us.
     */
public List<Player> getRebels() {
    List<Player> rebels = new ArrayList<Player>();
    for (Player p : getGame().getLiveEuropeanPlayers()) {
        if (p.getREFPlayer() == this && (p.getPlayerType() == PlayerType.REBEL || p.getPlayerType() == PlayerType.UNDEAD))
            rebels.add(p);
    }
    return rebels;
}
