// Serialization 
/**
     * {@inheritDoc}
     */
@Override
protected void toXMLImpl(XMLStreamWriter out) throws XMLStreamException {
    out.writeStartElement(getXMLElementTagName());
    super.writeAttributes(out);
    for (AIGoods ag : aiGoods) {
        if (!ag.checkIntegrity())
            continue;
        out.writeStartElement(AIGoods.getXMLElementTagName() + LIST_ELEMENT);
        out.writeAttribute(ID_ATTRIBUTE, ag.getId());
        out.writeEndElement();
    }
    for (Wish w : wishes) {
        String tag = (w instanceof GoodsWish) ? GoodsWish.getXMLElementTagName() : (w instanceof WorkerWish) ? WorkerWish.getXMLElementTagName() : null;
        if (!w.checkIntegrity() || !w.shouldBeStored() || tag == null)
            continue;
        out.writeStartElement(tag + LIST_ELEMENT);
        out.writeAttribute(ID_ATTRIBUTE, w.getId());
        out.writeEndElement();
    }
    for (TileImprovementPlan tip : tileImprovementPlans) {
        if (!tip.checkIntegrity())
            continue;
        out.writeStartElement(TileImprovementPlan.getXMLElementTagName() + LIST_ELEMENT);
        out.writeAttribute(ID_ATTRIBUTE, tip.getId());
        out.writeEndElement();
    }
    out.writeEndElement();
}
