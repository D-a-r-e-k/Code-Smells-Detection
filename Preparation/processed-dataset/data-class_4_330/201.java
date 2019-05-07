/**
     * Initialize this object from an XML-representation of this object.
     *
     * @param in The input stream with the XML.
     */
protected void readAttributes(XMLStreamReader in) throws XMLStreamException {
    super.readAttributes(in);
    name = in.getAttributeValue(null, "username");
    nationID = in.getAttributeValue(null, "nationID");
    if (!isUnknownEnemy()) {
        nationType = getSpecification().getNationType(in.getAttributeValue(null, "nationType"));
    }
    admin = getAttribute(in, "admin", false);
    gold = Integer.parseInt(in.getAttributeValue(null, "gold"));
    immigration = getAttribute(in, "immigration", 0);
    liberty = getAttribute(in, "liberty", 0);
    interventionBells = getAttribute(in, "interventionBells", 0);
    oldSoL = getAttribute(in, "oldSoL", 0);
    score = getAttribute(in, "score", 0);
    ready = getAttribute(in, "ready", false);
    ai = getAttribute(in, "ai", false);
    dead = getAttribute(in, "dead", false);
    bankrupt = getAttribute(in, "bankrupt", false);
    tax = Integer.parseInt(in.getAttributeValue(null, "tax"));
    playerType = Enum.valueOf(PlayerType.class, in.getAttributeValue(null, "playerType"));
    currentFather = getSpecification().getType(in, "currentFather", FoundingFather.class, null);
    immigrationRequired = getAttribute(in, "immigrationRequired", 12);
    newLandName = getAttribute(in, "newLandName", (String) null);
    independentNationName = getAttribute(in, "independentNationName", (String) null);
    attackedByPrivateers = getAttribute(in, "attackedByPrivateers", false);
    final String entryLocationStr = in.getAttributeValue(null, "entryLocation");
    if (entryLocationStr != null) {
        FreeColGameObject fcgo = getGame().getFreeColGameObject(entryLocationStr);
        entryLocation = (fcgo instanceof Location) ? (Location) fcgo : new Tile(getGame(), entryLocationStr);
    }
    for (RegionType regionType : RegionType.values()) {
        String key = regionType.getNameIndexKey();
        int index = getAttribute(in, key, -1);
        if (index > 0)
            setNameIndex(key, index);
    }
    if (nationType != null)
        addFeatures(nationType);
    switch(playerType) {
        case REBEL:
        case INDEPENDENT:
            addAbility(new Ability("model.ability.independenceDeclared"));
            break;
        default:
            // no special abilities for other playertypes, but silent warning about unused enum. 
            break;
    }
    tension.clear();
    stance.clear();
    allFathers.clear();
    offeredFathers.clear();
    europe = null;
    monarch = null;
    history.clear();
    tradeRoutes.clear();
    modelMessages.clear();
    lastSales = null;
    highSeas = null;
}
