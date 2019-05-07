/**
     * Tries to apply a colony plan given a list of workers.
     *
     * @param workers A list of <code>Unit</code>s to assign.
     * @param preferScout Prefer to make scouts rather than soldiers.
     * @return A scratch colony with the workers in place.
     */
public Colony assignWorkers(List<Unit> workers, boolean preferScout) {
    final GoodsType foodType = spec().getPrimaryFoodType();
    final int maxUnitFood = colony.getOwner().getMaximumFoodConsumption();
    final Turn turn = aiMain.getGame().getTurn();
    // Collect the work location plans.  Note that the plans are 
    // pre-sorted in order of desirability. 
    final List<GoodsType> produce = getPreferredProduction();
    List<WorkLocationPlan> foodPlans = getFoodPlans();
    List<WorkLocationPlan> workPlans = getWorkPlans();
    // Make a scratch colony to work on. 
    Colony scratch = colony.getScratchColony();
    Tile tile = scratch.getTile();
    String report = "Worker assignment at " + colony.getName() + " of " + workers.size() + " workers " + " in " + turn + "/" + turn.getNumber() + "\n";
    // Move all workers to the tile, removing storable equipment. 
    for (Unit u : workers) {
        TypeCountMap<EquipmentType> equipment = u.getEquipment();
        u.setLocation(tile);
        for (EquipmentType e : new ArrayList<EquipmentType>(equipment.keySet())) {
            int n = equipment.getCount(e);
            u.changeEquipment(e, -n);
            scratch.addEquipmentGoods(e, n);
        }
    }
    // Move outdoor experts outside if possible. 
    // Prefer scouts in early game if there are very few. 
    final Role outdoorRoles[] = new Role[] { Role.PIONEER, Role.SOLDIER, Role.SCOUT };
    if (preferScout) {
        outdoorRoles[1] = Role.SCOUT;
        outdoorRoles[2] = Role.SOLDIER;
    }
    for (int j = 0; j < outdoorRoles.length; j++) {
        String ability = "model.ability.expert" + outdoorRoles[j].toString().substring(0, 1) + outdoorRoles[j].toString().substring(1).toLowerCase();
        for (Unit u : new ArrayList<Unit>(workers)) {
            if (workers.size() <= 1)
                break;
            if (u.hasAbility(ability) && equipUnit(u, outdoorRoles[j], scratch)) {
                workers.remove(u);
                report += u.getId() + "(" + u.getType().toString().substring(11) + ") -> " + outdoorRoles[j] + "\n";
            }
        }
    }
    // Consider the defence situation. 
    // TODO: scan for neighbouring hostiles 
    // Favour low-skill/experience units for defenders, order experts 
    // in reverse order of their production on the produce-list. 
    Comparator<Unit> soldierComparator = new Comparator<Unit>() {

        public int compare(Unit u1, Unit u2) {
            int cmp = u1.getSkillLevel() - u2.getSkillLevel();
            if (cmp != 0)
                return cmp;
            GoodsType g1 = u1.getType().getExpertProduction();
            GoodsType g2 = u2.getType().getExpertProduction();
            if (g1 != null && g2 != null) {
                return produce.indexOf(g2) - produce.indexOf(g1);
            }
            return u1.getExperience() - u2.getExperience();
        }
    };
    Collections.sort(workers, soldierComparator);
    for (Unit u : new ArrayList<Unit>(workers)) {
        if (workers.size() <= 1)
            break;
        if (!AIColony.isBadlyDefended(scratch))
            break;
        if (equipUnit(u, Role.SOLDIER, scratch)) {
            workers.remove(u);
            report += u.getId() + "(" + u.getType().toString().substring(11) + ") -> SOLDIER\n";
        }
    }
    // Greedy assignment of other workers to plans. 
    List<AbstractGoods> buildGoods = new ArrayList<AbstractGoods>();
    BuildableType build = colony.getCurrentlyBuilding();
    if (build != null)
        buildGoods.addAll(build.getRequiredGoods());
    List<WorkLocationPlan> wlps;
    WorkLocationPlan wlp;
    boolean done = false;
    while (!workers.isEmpty() && !done) {
        // Decide what to produce: set the work location plan to 
        // try (wlp), and the list the plan came from so it can 
        // be recycled if successful (wlps). 
        wlps = null;
        wlp = null;
        if (scratch.getAdjustedNetProductionOf(foodType) > 0) {
            // Try to produce something. 
            wlps = workPlans;
            while (!produce.isEmpty()) {
                if ((wlp = findPlan(produce.get(0), workPlans)) != null) {
                    break;
                }
                produce.remove(0);
            }
        }
        // See if a plan can be satisfied. 
        Unit best;
        WorkLocation wl;
        GoodsType goodsType;
        for (; ; ) {
            if (wlp == null) {
                // Time to use a food plan. 
                if (foodPlans.isEmpty()) {
                    report += "Food plans exhausted\n";
                    done = true;
                    break;
                }
                wlps = foodPlans;
                wlp = wlps.get(0);
            }
            String err = null;
            goodsType = wlp.getGoodsType();
            wl = wlp.getWorkLocation();
            wl = scratch.getCorrespondingWorkLocation(wl);
            best = null;
            report += String.format("%-2d: %15s@%-25s => ", scratch.getUnitCount(), goodsType.toString().substring(12), ((wl instanceof Building) ? ((Building) wl).getType().toString().substring(15) : (wl instanceof ColonyTile) ? (((ColonyTile) wl).getWorkTile().getPosition().toString() + ((ColonyTile) wl).getWorkTile().getType().toString().substring(11)) : wl.toString()));
            if (!wl.canBeWorked()) {
                err = "can not be worked";
            } else if (wl.isFull()) {
                err = "full";
            } else if ((best = getBestWorker(wl, goodsType, workers)) == null) {
                err = "no worker found";
            }
            if (err != null) {
                wlps.remove(wlp);
                // The plan can not be worked, dump it. 
                report += err + "\n";
                break;
            }
            // Found a suitable worker, place it. 
            best.setLocation(wl);
            // Did the placement break the production bonus? 
            if (scratch.getProductionBonus() < 0) {
                best.setLocation(tile);
                done = true;
                report += "broke production bonus\n";
                break;
            }
            // Is the colony going to starve because of this placement? 
            if (scratch.getAdjustedNetProductionOf(foodType) < 0) {
                int net = scratch.getAdjustedNetProductionOf(foodType);
                int count = scratch.getGoodsCount(foodType);
                if (count / -net < PRODUCTION_TURNOVER_TURNS) {
                    // Too close for comfort.  Back out the 
                    // placement and try a food plan, unless this 
                    // was already a food plan. 
                    best.setLocation(tile);
                    wlp = null;
                    if (goodsType.isFoodType()) {
                        report += "starvation (" + count + "/" + net + ")\n";
                        done = true;
                        break;
                    }
                    report += "would starve (" + count + "/" + net + ")\n";
                    continue;
                }
            }
            // Check if placing the worker will soon exhaust the 
            // raw material.  Do not reduce raw materials below 
            // what is needed for a building--- e.g. prevent 
            // musket production from hogging the tools. 
            GoodsType raw = goodsType.getRawMaterial();
            int rawNeeded = 0;
            for (AbstractGoods ag : buildGoods) {
                if (raw == ag.getType())
                    rawNeeded += ag.getAmount();
            }
            if (raw == null || scratch.getAdjustedNetProductionOf(raw) >= 0 || (((scratch.getGoodsCount(raw) - rawNeeded) / -scratch.getAdjustedNetProductionOf(raw)) >= PRODUCTION_TURNOVER_TURNS)) {
                // No raw material problems, the placement 
                // succeeded.  Set the work type, move the 
                // successful goods type to the end of the produce 
                // list for later reuse, remove the worker from 
                // the workers pool, but leave the successful plan 
                // on its list. 
                best.setWorkType(goodsType);
                workers.remove(best);
                report += best.getId() + "(" + best.getType().toString().substring(11) + ")\n";
                if (!goodsType.isFoodType() && produce.remove(goodsType)) {
                    produce.add(goodsType);
                }
                break;
            }
            // Yes, we need more of the raw material.  Pull the 
            // unit out again and see if we can make more. 
            best.setLocation(tile);
            WorkLocationPlan rawWlp = findPlan(raw, workPlans);
            if (rawWlp != null) {
                // OK, we have an alternate plan.  Put the raw 
                // material at the start of the produce list and 
                // loop trying to satisfy the alternate plan. 
                if (produce.remove(raw))
                    produce.add(0, raw);
                wlp = rawWlp;
                report += "retry with " + raw.toString().substring(12) + "\n";
                continue;
            }
            // No raw material available, so we have to give up on 
            // both the plan and the type of production. 
            // Hopefully the raw production is positive again and 
            // we will succeed next time. 
            wlps.remove(wlp);
            produce.remove(goodsType);
            report += "needs more " + raw.toString().substring(12) + "\n";
            break;
        }
    }
    // Put the rest of the workers on the tile. 
    for (Unit u : workers) {
        if (u.getLocation() != tile)
            u.setLocation(tile);
    }
    // Check for failure to assign any workers.  This happens when: 
    // - there are no useful food plans 
    //   - in which case look for a `harmless' place and add one worker 
    // - food is low, and perhaps partly eaten by horses, and no 
    //   unit can *improve* production by being added. 
    //   - find a place to produce food that at least avoids 
    //     starvation and add one worker. 
    if (scratch.getWorkLocationUnitCount() == 0) {
        if (getFoodPlans().isEmpty()) {
            locations: for (WorkLocation wl : scratch.getAvailableWorkLocations()) {
                for (Unit u : new ArrayList<Unit>(workers)) {
                    for (GoodsType type : libertyGoodsTypes) {
                        if (wl.canAdd(u) && wl.getPotentialProduction(type, u.getType()) > 0) {
                            u.setLocation(wl);
                            u.setWorkType(type);
                            break locations;
                        }
                    }
                }
            }
        } else {
            plans: for (WorkLocationPlan w : getFoodPlans()) {
                GoodsType goodsType = w.getGoodsType();
                WorkLocation wl = w.getWorkLocation();
                for (Unit u : new ArrayList<Unit>(workers)) {
                    GoodsType oldWork = u.getWorkType();
                    u.setLocation(wl);
                    u.setWorkType(goodsType);
                    if (scratch.getAdjustedNetProductionOf(foodType) >= 0) {
                        report += "Subsist with " + u + "\n";
                        workers.remove(u);
                        break plans;
                    }
                    u.setLocation(tile);
                    u.setWorkType(oldWork);
                }
            }
        }
    }
    // The greedy algorithm works reasonably well, but will 
    // misplace experts when they are more productive at the 
    // immediately required task than a lesser unit, not knowing 
    // that a requirement for their speciality will subsequently 
    // follow.  Do a cleanup pass to sort these out. 
    List<Unit> experts = new ArrayList<Unit>();
    List<Unit> nonExperts = new ArrayList<Unit>();
    for (Unit u : scratch.getUnitList()) {
        if (u.getType().getExpertProduction() != null) {
            if (u.getType().getExpertProduction() != u.getWorkType()) {
                experts.add(u);
            }
        } else {
            nonExperts.add(u);
        }
    }
    int expert = 0;
    while (expert < experts.size()) {
        Unit u1 = experts.get(expert);
        Unit other;
        if ((other = trySwapExpert(u1, experts)) != null) {
            report += "Swapped " + u1.getId() + "(" + u1.getType().toString().substring(11) + ") for " + other + "\n";
            experts.remove(u1);
        } else if ((other = trySwapExpert(u1, nonExperts)) != null) {
            report += "Swapped " + u1.getId() + "(" + u1.getType().toString().substring(11) + ") for " + other + "\n";
            experts.remove(u1);
        } else {
            expert++;
        }
    }
    for (Unit u : tile.getUnitList()) {
        GoodsType work = u.getType().getExpertProduction();
        if (work != null) {
            Unit other = trySwapExpert(u, scratch.getUnitList());
            if (other != null) {
                report += "Swapped " + u.getId() + "(" + u.getType().toString().substring(11) + ") for " + other + "\n";
            }
        }
    }
    // Rearm what remains as far as possible. 
    workers.clear();
    for (Unit u : tile.getUnitList()) {
        if (u.getEquipment().isEmpty())
            workers.add(u);
    }
    Collections.sort(workers, soldierComparator);
    for (Unit u : workers) {
        if (equipUnit(u, Role.SOLDIER, scratch)) {
            report += u.getId() + "(" + u.getType().toString().substring(11) + ") -> SOLDIER\n";
        }
    }
    // Log and return the scratch colony on success. 
    // Otherwise abandon this rearrangement, disposing of the 
    // scratch colony and returning null. 
    report += "Final population = " + scratch.getWorkLocationUnitCount();
    logger.finest(report);
    if (scratch.getWorkLocationUnitCount() > 0)
        return scratch;
    scratch.disposeScratchColony();
    logger.warning("assignWorkers at " + colony.getName() + " failed.");
    return null;
}
