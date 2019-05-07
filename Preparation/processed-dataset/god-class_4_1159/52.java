/**
     * Checks whether this player is at war with any other player.
     *
     * @return <i>true</i> if this player is at war with any other.
     */
public boolean isAtWar() {
    for (Player player : getGame().getPlayers()) {
        if (atWarWith(player))
            return true;
    }
    return false;
}
