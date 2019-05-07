/**
     * {@inheritDoc}
     */
@Override
protected void readChild(XMLStreamReader in) throws XMLStreamException {
    final AIMain aiMain = getAIMain();
    final String tag = in.getLocalName();
    if (tag.equals(AIGoods.getXMLElementTagName() + LIST_ELEMENT)) {
        String str = in.getAttributeValue(null, ID_ATTRIBUTE);
        AIGoods ag = (AIGoods) aiMain.getAIObject(str);
        if (ag == null)
            ag = new AIGoods(aiMain, str);
        aiGoods.add(ag);
    } else if (tag.equals(TileImprovementPlan.getXMLElementTagName() + LIST_ELEMENT) || in.getLocalName().equals("tileimprovementplan" + LIST_ELEMENT)) {
        String str = in.getAttributeValue(null, ID_ATTRIBUTE);
        TileImprovementPlan ti = (TileImprovementPlan) aiMain.getAIObject(str);
        if (ti == null)
            ti = new TileImprovementPlan(aiMain, str);
        tileImprovementPlans.add(ti);
    } else if (tag.equals(GoodsWish.getXMLElementTagName() + LIST_ELEMENT) || in.getLocalName().equals(GoodsWish.getXMLElementTagName() + "Wish" + LIST_ELEMENT)) {
        String str = in.getAttributeValue(null, ID_ATTRIBUTE);
        GoodsWish gw = (GoodsWish) aiMain.getAIObject(str);
        if (gw == null)
            gw = new GoodsWish(aiMain, str);
        wishes.add(gw);
    } else if (tag.equals(WorkerWish.getXMLElementTagName() + LIST_ELEMENT) || in.getLocalName().equals(WorkerWish.getXMLElementTagName() + "Wish" + LIST_ELEMENT)) {
        String str = in.getAttributeValue(null, ID_ATTRIBUTE);
        Wish ww = (Wish) aiMain.getAIObject(str);
        if (ww == null)
            ww = new WorkerWish(aiMain, str);
        wishes.add(ww);
    } else {
        logger.warning("Unknown tag name: " + in.getLocalName());
    }
    in.nextTag();
}
