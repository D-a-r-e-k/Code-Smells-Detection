/**
     * Refines this plan given the colony choice of what to build.
     *
     * @param build The <code>BuildableType</code> to be built (may be null).
     */
public void refine(BuildableType build) {
    List<GoodsType> required = new ArrayList<GoodsType>();
    for (AbstractGoods ag : getRequiredGoods(build)) {
        required.add(ag.getType());
    }
    Map<GoodsType, List<WorkLocationPlan>> suppressed = new HashMap<GoodsType, List<WorkLocationPlan>>();
    // Examine a copy of the work plans, but operate on the 
    // original list.  Maintain a offset between the index in the 
    // copied list and the original to aid reinsertion. 
    // 
    // Remove any work plans to make raw/building goods that are 
    // not required to complete the current buildable, but take 
    // care to put such plans back again if a plan is encountered 
    // that makes goods that are made from a type that was removed 
    // and there is less than CARGO_SIZE/2 of that type in stock. 
    // Note though in such cases the position of the 
    // building-goods plans in the work plans list will have moved 
    // from their usual high priority to immediately before the 
    // position of the manufactured goods. 
    // 
    // So, for example, we should suppress tool building when a 
    // colony is building a warehouse, unless we find a plan to 
    // make muskets and the tool stock is low. 
    // 
    // TODO: generalize this further to make tools for pioneers. 
    // 
    List<WorkLocationPlan> plans = new ArrayList<WorkLocationPlan>(workPlans);
    int offset = 0;
    for (int i = 0; i < plans.size(); i++) {
        List<WorkLocationPlan> wls;
        WorkLocationPlan wlp = plans.get(i);
        GoodsType g = wlp.getGoodsType();
        if ((rawBuildingGoodsTypes.contains(g) && !required.contains(g.getProducedMaterial())) || (buildingGoodsTypes.contains(g) && !required.contains(g))) {
            workPlans.remove(i - offset);
            offset++;
            wls = suppressed.get(g);
            if (wls == null)
                wls = new ArrayList<WorkLocationPlan>();
            wls.add(0, wlp);
            // reverses list 
            suppressed.put(g, wls);
            produce.remove(g);
            logger.finest("At " + colony.getName() + " suppress production of " + g);
        } else if (g.isRefined() && (rawBuildingGoodsTypes.contains(g.getRawMaterial()) || buildingGoodsTypes.contains(g.getRawMaterial()))) {
            int n = 0, idx = produce.indexOf(g);
            for (GoodsType type = g.getRawMaterial(); type != null; type = type.getRawMaterial()) {
                if ((wls = suppressed.get(type)) == null)
                    break;
                if (colony.getGoodsCount(type) >= GoodsContainer.CARGO_SIZE / 2)
                    break;
                n += wls.size();
                while (!wls.isEmpty()) {
                    // reverses again when adding, cancelling reversal above 
                    workPlans.add(i - offset, wls.remove(0));
                }
                produce.add(idx, type);
                logger.finest("At " + colony.getName() + " restore production of " + type);
            }
            offset -= n;
        }
    }
}
