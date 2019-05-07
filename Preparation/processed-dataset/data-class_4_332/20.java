/**
     * Tries to complete a supplied wish.
     *
     * @param wish The <code>Wish</code> to complete.
     * @return True if this wish was successfully completed.
     */
public boolean completeWish(Wish wish, String reason) {
    if (!wishes.remove(wish))
        return false;
    ((EuropeanAIPlayer) getAIOwner()).completeWish(wish);
    logger.finest(colony.getName() + " completes " + reason + " wish: " + wish);
    wish.dispose();
    return true;
}
