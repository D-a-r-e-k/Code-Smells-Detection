/**
     * Checks if the player has enough gold to make a purchase.
     * Use this rather than comparing with getGold(), as this handles
     * players that do not account for gold.
     *
     * @param amount The purchase price to check.
     * @return True if the player can afford the purchase.
     */
public boolean checkGold(int amount) {
    return this.gold == GOLD_NOT_ACCOUNTED || this.gold >= amount;
}
