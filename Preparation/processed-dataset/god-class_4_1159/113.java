/**
     * Modifies the amount of gold that this player has. The argument can be
     * both positive and negative.
     *
     * @param amount The amount of gold to be added to this player.
     * @return The amount of gold post-modification.
     */
public int modifyGold(int amount) {
    if (this.gold != Player.GOLD_NOT_ACCOUNTED) {
        if ((gold + amount) >= 0) {
            modifyScore((gold + amount) / 1000 - gold / 1000);
            gold += amount;
        } else {
            // This can happen if the server and the client get 
            // out of sync.  Perhaps it can also happen if the 
            // client tries to adjust gold for another player, 
            // where the balance is unknown. Just keep going and 
            // do the best thing possible, we don't want to crash 
            // the game here. 
            logger.warning("Cannot add " + amount + " gold for " + this + ": would be negative!");
            gold = 0;
        }
    }
    return gold;
}
