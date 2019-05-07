/**
     * Disposes this <code>AIColony</code>.
     */
public void dispose() {
    List<AIObject> disposeList = new ArrayList<AIObject>();
    for (AIGoods ag : aiGoods) {
        if (ag.getGoods().getLocation() == colony)
            disposeList.add(ag);
    }
    for (Wish w : wishes) {
        disposeList.add(w);
    }
    for (TileImprovementPlan ti : tileImprovementPlans) {
        disposeList.add(ti);
    }
    for (AIObject o : disposeList) o.dispose();
    colonyPlan = null;
    // Do not clear this.colony, the id is still required. 
    super.dispose();
}
