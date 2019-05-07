/**
     * Gets the best worker to execute a work location plan.
     * - The most productive one wins (which will automatically pick a
     *   relevant expert).
     * - If they are all relevant experts, pick any.
     * - Pick the unit that can upgrade to the required expert with the most
     *     relevant experience or least irrelevant expertise.
     * - Pick a unit that can not upgrade at all.
     * - Pick an otherwise upgradeable unit with the most relevant experience
     *     or least irrelevant experience.
     * - Pick the least skillful unit.
     *
     * Public for the benefit of the test suite.
     *
     * @param wl The <code>WorkLocation</code> to work at.
     * @param goodsType The <code>GoodsType</code> to make.
     * @param workers A list of potential <code>Unit</code>s to try.
     * @return The best worker for the job.
     */
public static Unit getBestWorker(WorkLocation wl, GoodsType goodsType, List<Unit> workers) {
    if (workers == null || workers.isEmpty())
        return null;
    final Colony colony = wl.getColony();
    final GoodsType outputType = (goodsType.isStoredAs()) ? goodsType.getStoredAs() : goodsType;
    // Avoid some nasty autodestructions by accepting singleton 
    // workers that do *something*. 
    if (workers.size() == 1) {
        Unit u = workers.get(0);
        if (!wl.canAdd(u))
            return null;
        Location oldLoc = u.getLocation();
        GoodsType oldWork = u.getWorkType();
        u.setLocation(wl);
        u.setWorkType(goodsType);
        int production = wl.getProductionOf(u, goodsType);
        u.setLocation(oldLoc);
        u.setWorkType(oldWork);
        return (production > 0) ? u : null;
    }
    // Do not mutate the workers list! 
    List<Unit> todo = new ArrayList<Unit>(workers);
    List<Unit> best = new ArrayList<Unit>();
    int bestValue = colony.getAdjustedNetProductionOf(outputType);
    Unit special = null;
    best.clear();
    for (Unit u : todo) {
        if (!wl.canAdd(u))
            continue;
        Location oldLoc = u.getLocation();
        GoodsType oldWork = u.getWorkType();
        u.setLocation(wl);
        u.setWorkType(goodsType);
        int value = colony.getAdjustedNetProductionOf(outputType);
        if (value > bestValue) {
            bestValue = value;
            best.clear();
            best.add(u);
            if (u.getType().getExpertProduction() == goodsType) {
                special = u;
            }
        } else if (value == bestValue && !best.isEmpty()) {
            best.add(u);
            if (u.getType().getExpertProduction() == goodsType) {
                special = u;
            }
        }
        u.setLocation(oldLoc);
        u.setWorkType(oldWork);
    }
    switch(best.size()) {
        case 0:
            return null;
        // Not good.  No unit improves production. 
        case 1:
            return best.get(0);
        default:
            todo.clear();
            todo.addAll(best);
            break;
    }
    // Several winners including an expert implies they are all experts. 
    if (special != null)
        return special;
    // Partition units into those that can upgrade-by-experience 
    // to the relevant expert (which we favour), those that can 
    // upgrade-by-experience in some way but not to the expert 
    // (which we avoid), and the rest.  Within the groups, favour 
    // those with the most relevant experience and the least irrelevant 
    // experience. 
    Specification spec = colony.getSpecification();
    UnitType expert = spec.getExpertForProducing(goodsType);
    best.clear();
    bestValue = Integer.MIN_VALUE;
    for (Unit u : todo) {
        boolean relevant = u.getWorkType() == goodsType;
        int score = (relevant) ? u.getExperience() : -u.getExperience();
        if (expert != null && u.getType().canBeUpgraded(expert, ChangeType.EXPERIENCE)) {
            score += 10000;
        } else if (expert != null && u.getType().canBeUpgraded(null, ChangeType.EXPERIENCE)) {
            score -= 10000;
        }
        if (score > bestValue) {
            best.clear();
            best.add(u);
            bestValue = score;
        } else if (score == bestValue) {
            best.add(u);
        }
    }
    switch(best.size()) {
        case 0:
            break;
        case 1:
            return best.get(0);
        default:
            todo.clear();
            todo.addAll(best);
            break;
    }
    // Use the unit with the least skill, in the hope that 
    // remaining experts will be called upon in due course. 
    int worstSkill = Integer.MAX_VALUE;
    special = null;
    for (Unit u : todo) {
        if (u.getType().getSkill() < worstSkill) {
            special = u;
            worstSkill = u.getType().getSkill();
        }
    }
    return special;
}
