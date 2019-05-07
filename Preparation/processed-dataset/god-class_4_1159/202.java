protected void readChildren(XMLStreamReader in) throws XMLStreamException {
    while (in.nextTag() != XMLStreamConstants.END_ELEMENT) {
        if (in.getLocalName().equals(TENSION_TAG)) {
            Player player = getGame().getFreeColGameObject(in.getAttributeValue(null, "player"), Player.class);
            tension.put(player, new Tension(getAttribute(in, VALUE_TAG, 0)));
            in.nextTag();
        } else if (in.getLocalName().equals(FOUNDING_FATHER_TAG)) {
            int length = Integer.parseInt(in.getAttributeValue(null, ARRAY_SIZE));
            for (int index = 0; index < length; index++) {
                String fatherId = in.getAttributeValue(null, "x" + String.valueOf(index));
                FoundingFather father = getSpecification().getFoundingFather(fatherId);
                addFather(father);
            }
            in.nextTag();
        } else if (in.getLocalName().equals(OFFERED_FATHER_TAG)) {
            int length = Integer.parseInt(in.getAttributeValue(null, ARRAY_SIZE));
            for (int index = 0; index < length; index++) {
                String fatherId = in.getAttributeValue(null, "x" + String.valueOf(index));
                FoundingFather father = getSpecification().getFoundingFather(fatherId);
                offeredFathers.add(father);
            }
            in.nextTag();
        } else if (in.getLocalName().equals(STANCE_TAG)) {
            String playerId = in.getAttributeValue(null, "player");
            stance.put(playerId, Enum.valueOf(Stance.class, in.getAttributeValue(null, VALUE_TAG)));
            in.nextTag();
        } else if (in.getLocalName().equals(HighSeas.getXMLElementTagName())) {
            highSeas = updateFreeColGameObject(in, HighSeas.class);
        } else if (in.getLocalName().equals(Europe.getXMLElementTagName())) {
            europe = updateFreeColGameObject(in, Europe.class);
        } else if (in.getLocalName().equals(Monarch.getXMLElementTagName())) {
            monarch = updateFreeColGameObject(in, Monarch.class);
        } else if (in.getLocalName().equals(HistoryEvent.getXMLElementTagName())) {
            HistoryEvent event = new HistoryEvent();
            event.readFromXML(in);
            getHistory().add(event);
        } else if (in.getLocalName().equals(TradeRoute.getXMLElementTagName())) {
            TradeRoute route = updateFreeColGameObject(in, TradeRoute.class);
            tradeRoutes.add(route);
        } else if (in.getLocalName().equals(Market.getXMLElementTagName())) {
            market = updateFreeColGameObject(in, Market.class);
        } else if (in.getLocalName().equals(ModelMessage.getXMLElementTagName())) {
            ModelMessage message = new ModelMessage();
            message.readFromXML(in);
            addModelMessage(message);
        } else if (in.getLocalName().equals(LastSale.getXMLElementTagName())) {
            LastSale lastSale = new LastSale();
            lastSale.readFromXML(in);
            saveSale(lastSale);
        } else if (Modifier.getXMLElementTagName().equals(in.getLocalName())) {
            addModifier(new Modifier(in, getSpecification()));
        } else {
            logger.warning("Unknown tag: " + in.getLocalName() + " loading player");
            in.nextTag();
        }
    }
    // sanity check: we should be on the closing tag 
    if (!in.getLocalName().equals(Player.getXMLElementTagName())) {
        logger.warning("Error parsing xml: expecting closing tag </" + Player.getXMLElementTagName() + "> " + "found instead: " + in.getLocalName());
    }
    // TODO: This should no longer happen.  Remove soon (early 2012) 
    // if further testing never triggers the following warning. 
    if (market == null) {
        logger.warning("Null market for " + getName());
        Thread.dumpStack();
        market = new Market(getGame(), this);
    }
    // Bells bonuses depend on tax 
    recalculateBellsBonus();
    invalidateCanSeeTiles();
}
