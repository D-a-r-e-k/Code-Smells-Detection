protected void writeChildren(XMLStreamWriter out, Player player, boolean showAll, boolean toSavedGame) throws XMLStreamException {
    if (market != null) {
        market.toXML(out, player, showAll, toSavedGame);
    }
    if (showAll || toSavedGame || equals(player)) {
        for (Entry<Player, Tension> entry : tension.entrySet()) {
            out.writeStartElement(TENSION_TAG);
            out.writeAttribute("player", entry.getKey().getId());
            out.writeAttribute(VALUE_TAG, String.valueOf(entry.getValue().getValue()));
            out.writeEndElement();
        }
        for (Entry<String, Stance> entry : stance.entrySet()) {
            out.writeStartElement(STANCE_TAG);
            out.writeAttribute("player", entry.getKey());
            out.writeAttribute(VALUE_TAG, entry.getValue().toString());
            out.writeEndElement();
        }
        for (HistoryEvent event : history) {
            event.toXML(out);
        }
        for (TradeRoute route : tradeRoutes) {
            route.toXML(out, this, false, false);
        }
        if (highSeas != null) {
            highSeas.toXMLImpl(out, player, showAll, toSavedGame);
        }
        out.writeStartElement(FOUNDING_FATHER_TAG);
        out.writeAttribute(ARRAY_SIZE, Integer.toString(allFathers.size()));
        int index = 0;
        for (FoundingFather father : allFathers) {
            out.writeAttribute("x" + Integer.toString(index), father.getId());
            index++;
        }
        out.writeEndElement();
        out.writeStartElement(OFFERED_FATHER_TAG);
        out.writeAttribute(ARRAY_SIZE, Integer.toString(offeredFathers.size()));
        index = 0;
        for (FoundingFather father : offeredFathers) {
            out.writeAttribute("x" + Integer.toString(index), father.getId());
            index++;
        }
        out.writeEndElement();
        if (europe != null) {
            europe.toXML(out, player, showAll, toSavedGame);
        }
        if (monarch != null) {
            monarch.toXML(out, player, showAll, toSavedGame);
        }
        if (!modelMessages.isEmpty()) {
            for (ModelMessage m : modelMessages) {
                m.toXML(out);
            }
        }
        if (lastSales != null) {
            for (LastSale sale : lastSales.values()) {
                sale.toXMLImpl(out);
            }
        }
        Turn turn = getGame().getTurn();
        for (Modifier modifier : getModifiers()) {
            if (modifier.isTemporary() && !modifier.isOutOfDate(turn)) {
                modifier.toXML(out);
            }
        }
    } else {
        Tension t = getTension(player);
        if (t != null) {
            out.writeStartElement(TENSION_TAG);
            out.writeAttribute("player", player.getId());
            out.writeAttribute(VALUE_TAG, String.valueOf(t.getValue()));
            out.writeEndElement();
        }
        Stance s = getStance(player);
        for (Entry<String, Stance> entry : stance.entrySet()) {
            out.writeStartElement(STANCE_TAG);
            out.writeAttribute("player", player.getId());
            out.writeAttribute(VALUE_TAG, s.toString());
            out.writeEndElement();
        }
    }
}
