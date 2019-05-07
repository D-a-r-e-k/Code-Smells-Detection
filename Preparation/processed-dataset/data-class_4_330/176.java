/**
     * Returns true if type of goods can be traded at specified place.
     *
     * @param type The GoodsType.
     * @param access The way the goods are traded (Europe OR Custom)
     * @return <code>true</code> if type of goods can be traded.
     */
public boolean canTrade(GoodsType type, Market.Access access) {
    if (getMarket().getArrears(type) == 0) {
        return true;
    }
    if (access == Market.Access.CUSTOM_HOUSE) {
        if (getSpecification().getBoolean(GameOptions.CUSTOM_IGNORE_BOYCOTT)) {
            return true;
        }
        if (hasAbility("model.ability.customHouseTradesWithForeignCountries")) {
            for (Player otherPlayer : getGame().getLiveEuropeanPlayers()) {
                if (otherPlayer != this && (getStance(otherPlayer) == Stance.PEACE || getStance(otherPlayer) == Stance.ALLIANCE)) {
                    return true;
                }
            }
        }
    }
    return false;
}
