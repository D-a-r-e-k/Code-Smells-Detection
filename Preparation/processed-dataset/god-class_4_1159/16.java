/**
     * What is the name of the player's market?
     * Following a declaration of independence we are assumed to trade
     * broadly with any European market rather than a specific port.
     *
     * @return A name for the player's market.
     */
public StringTemplate getMarketName() {
    return (getEurope() == null) ? StringTemplate.key("model.market.independent") : StringTemplate.key(nationID + ".europe");
}
