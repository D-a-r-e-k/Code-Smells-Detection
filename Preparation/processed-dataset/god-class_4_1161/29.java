/**
     * Updates the worker wishes.
     */
private void updateWorkerWishes() {
    final Specification spec = getSpecification();
    final int baseValue = 25;
    final int priorityMax = 50;
    final int priorityDecay = 5;
    final int multipleBonus = 5;
    final int multipleMax = 5;
    // For every non-expert, request expert replacement. 
    // Prioritize by lowest net production among the goods that are 
    // being produced by units (note that we have to traverse the work 
    // locations/unit-lists, rather than just check for non-zero 
    // production because it could be in balance). 
    // Add some weight when multiple cases of the same expert are 
    // needed, rather than generating heaps of wishes. 
    List<GoodsType> producing = new ArrayList<GoodsType>();
    for (WorkLocation wl : colony.getAvailableWorkLocations()) {
        for (Unit u : wl.getUnitList()) {
            GoodsType work = u.getWorkType();
            if (work != null) {
                work = work.getStoredAs();
                if (!producing.contains(work))
                    producing.add(work);
            }
        }
    }
    Collections.sort(producing, new Comparator<GoodsType>() {

        public int compare(GoodsType g1, GoodsType g2) {
            return colony.getAdjustedNetProductionOf(g1) - colony.getAdjustedNetProductionOf(g2);
        }
    });
    TypeCountMap<UnitType> experts = new TypeCountMap<UnitType>();
    for (Unit unit : colony.getUnitList()) {
        GoodsType goods = unit.getWorkType();
        UnitType expert = (goods == null || goods == unit.getType().getExpertProduction()) ? null : spec.getExpertForProducing(goods);
        if (expert != null) {
            experts.incrementCount(expert, 1);
        }
    }
    for (UnitType expert : experts.keySet()) {
        GoodsType goods = expert.getExpertProduction();
        int value = baseValue + Math.max(0, priorityMax - priorityDecay * producing.indexOf(goods)) + (Math.min(multipleMax, experts.getCount(expert) - 1) * multipleBonus);
        requireWorkerWish(expert, true, value);
    }
    // Request population increase if no worker wishes and the bonus 
    // can take it. 
    if (experts.isEmpty() && colony.governmentChange(colony.getWorkLocationUnitCount() + 1) >= 0) {
        boolean needFood = colony.getFoodProduction() <= colony.getFoodConsumption() + colony.getOwner().getMaximumFoodConsumption();
        // Choose expert for best work location plan 
        UnitType expert = spec.getDefaultUnitType();
        for (WorkLocationPlan plan : (needFood) ? colonyPlan.getFoodPlans() : colonyPlan.getWorkPlans()) {
            WorkLocation location = plan.getWorkLocation();
            if (!location.canBeWorked())
                continue;
            expert = spec.getExpertForProducing(plan.getGoodsType());
            break;
        }
        requireWorkerWish(expert, false, 50);
    }
    // TODO: check for students 
    // TODO: add missionaries 
    // Improve defence. 
    if (isBadlyDefended()) {
        UnitType bestDefender = colony.getBestDefenderType();
        if (bestDefender != null) {
            requireWorkerWish(bestDefender, true, 100);
        }
    }
}
