/**
     * {@inheritDoc}
     */
@Override
protected void readAttributes(XMLStreamReader in) throws XMLStreamException {
    final AIMain aiMain = getAIMain();
    final Game game = aiMain.getGame();
    String str = in.getAttributeValue(null, ID_ATTRIBUTE);
    if ((colony = game.getFreeColGameObject(str, Colony.class)) == null) {
        throw new IllegalStateException("Not a Colony: " + str);
    }
    aiGoods.clear();
    tileImprovementPlans.clear();
    wishes.clear();
    colonyPlan = new ColonyPlan(aiMain, colony);
}
