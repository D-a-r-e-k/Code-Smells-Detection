/**
     * Makes a plan for each type of possible production, that is
     * those work locations that can use a unit or can auto-produce.
     * Note that this will almost certainly include clashes over work
     * locations.  That gets sorted out elsewhere as ColonyPlans do
     * not examine the units present.
     *
     * With the complete list of work plans, finish creating the list
     * of goods to produce.
     *
     * Then filter out the auto-production plans as they are not
     * going to be helpful for unit allocation.
     *
     * Finally sort by desirability.
     */
private void updatePlans(Map<GoodsType, Map<WorkLocation, Integer>> production) {
    workPlans.clear();
    for (GoodsType g : production.keySet()) {
        // Do not make plans to produce into a full warehouse. 
        if (g.isStorable() && colony.getGoodsCount(g) >= colony.getWarehouseCapacity() && !g.limitIgnored())
            continue;
        for (WorkLocation wl : production.get(g).keySet()) {
            if (wl.canBeWorked() || wl.canAutoProduce()) {
                workPlans.add(new WorkLocationPlan(getAIMain(), wl, g));
            }
        }
    }
    // Now we have lots of plans, determine what goods to produce. 
    updateProductionList(production);
    // Filter out plans that can not use a unit. 
    List<WorkLocationPlan> oldPlans = new ArrayList<WorkLocationPlan>(workPlans);
    workPlans.clear();
    for (WorkLocationPlan wlp : oldPlans) {
        if (wlp.getWorkLocation().canBeWorked())
            workPlans.add(wlp);
    }
    // Sort the work plans by earliest presence in the produce 
    // list, and then by amount.  If the type of goods produced is 
    // not on the produce list, then make sure such plans sort to 
    // the end, except for food plans. 
    Collections.sort(workPlans, new Comparator<WorkLocationPlan>() {

        public int compare(WorkLocationPlan w1, WorkLocationPlan w2) {
            GoodsType g1 = w1.getGoodsType();
            GoodsType g2 = w2.getGoodsType();
            int i1 = produce.indexOf(g1);
            int i2 = produce.indexOf(g2);
            if (i1 < 0 && !g1.isFoodType())
                i1 = 99999;
            if (i2 < 0 && !g2.isFoodType())
                i2 = 99999;
            int cmp = i1 - i2;
            if (cmp == 0) {
                cmp = getWorkLocationProduction(w2.getWorkLocation(), g2) - getWorkLocationProduction(w1.getWorkLocation(), g1);
            }
            return cmp;
        }
    });
}
