/**
     * Rearranges the workers within this colony using the {@link ColonyPlan}.
     * TODO: Detect military threats and boost defence.
     *
     * @return True if the workers were rearranged.
     */
public boolean rearrangeWorkers() {
    // Skip this colony if it does not yet need rearranging, but 
    // first check if it is collapsing. 
    final int turn = getGame().getTurn().getNumber();
    if (colony.getWorkLocationUnitCount() <= 0) {
        avertAutoDestruction();
    } else if (rearrangeTurn.getNumber() > turn) {
        if (colony.getCurrentlyBuilding() == null && colonyPlan.getBestBuildableType() != null) {
            logger.warning(colony.getName() + " could be building but" + " is asleep until turn: " + rearrangeTurn.getNumber() + "( > " + turn + ")");
        }
        return false;
    }
    final Tile tile = colony.getTile();
    final AIMain aiMain = getAIMain();
    final Player player = colony.getOwner();
    final Specification spec = getSpecification();
    // For now, cap the rearrangement horizon, because confidence 
    // that we are triggering on all relevant changes is low. 
    int nextRearrange = 15;
    // See if there are neighbouring LCRs to explore, or tiles 
    // to steal, or just unclaimed tiles (a neighbouring settlement 
    // might have disappeared or relinquished a tile). 
    // This needs to be done early so that new tiles can be 
    // included in any new colony plan. 
    exploreLCRs();
    stealTiles();
    for (Tile t : tile.getSurroundingTiles(1)) {
        if (!player.owns(t) && player.canClaimForSettlement(t)) {
            AIMessage.askClaimLand(t, this, 0);
        }
    }
    // Update the colony plan. 
    colonyPlan.update();
    // Now that we know what raw materials are available in the 
    // colony plan, set the current buildable, first backing out 
    // of anything currently being built that is now impossible. 
    // If a buildable is chosen, refine the worker allocation in 
    // the colony plan in case the required building materials 
    // have changed. 
    BuildableType oldBuild = colony.getCurrentlyBuilding();
    BuildableType build = colonyPlan.getBestBuildableType();
    if (build != oldBuild) {
        List<BuildableType> queue = new ArrayList<BuildableType>();
        if (build != null)
            queue.add(build);
        AIMessage.askSetBuildQueue(this, queue);
        build = colony.getCurrentlyBuilding();
    }
    colonyPlan.refine(build);
    // Collect all potential workers from the colony and from the tile, 
    // being careful not to disturb existing non-colony missions. 
    // Note the special case of a unit aiming to build a colony on this 
    // tile, which happens regularly with the initial AI colony. 
    // Remember where the units came from. 
    List<Unit> workers = colony.getUnitList();
    List<UnitWas> was = new ArrayList<UnitWas>();
    for (Unit u : workers) was.add(new UnitWas(u));
    for (Unit u : tile.getUnitList()) {
        if (!u.isPerson() || getAIUnit(u) == null)
            continue;
        Mission mission = getAIUnit(u).getMission();
        if (mission == null || mission instanceof IdleAtSettlementMission || mission instanceof WorkInsideColonyMission || (mission instanceof BuildColonyMission && ((BuildColonyMission) mission).getTarget() == tile) || mission instanceof DefendSettlementMission) {
            workers.add(u);
            was.add(new UnitWas(u));
        }
    }
    // Assign the workers according to the colony plan. 
    // ATM we just accept this assignment unless it failed, in 
    // which case restore original state. 
    AIPlayer aiPlayer = getAIOwner();
    boolean preferScouts = ((EuropeanAIPlayer) aiPlayer).scoutsNeeded() > 0;
    Colony scratch = colonyPlan.assignWorkers(workers, preferScouts);
    if (scratch == null) {
        if (!UnitWas.revertAll(was)) {
            String complain = "Failed to revert:";
            for (UnitWas w : was) complain += " " + w.getUnit() + ",";
            logger.warning(complain.substring(0, complain.length() - 1));
        }
        rearrangeTurn = new Turn(turn + 1);
        return false;
    }
    // Apply the arrangement, and give suitable missions to all units. 
    // For now, do a soft rearrange (that is, no c-s messaging). 
    // Also change the goods counts as we may have changed equipment. 
    // TODO: Better would be to restore the initial state and use 
    // a special c-s message to execute the rearrangement--- code to 
    // untangle the movement dependencies is non-trivial. 
    for (Unit u : scratch.getUnitList()) {
        WorkLocation wl = (WorkLocation) u.getLocation();
        wl = colony.getCorrespondingWorkLocation(wl);
        u.setLocation(wl);
    }
    for (Unit u : scratch.getTile().getUnitList()) {
        u.setLocation(tile);
    }
    for (GoodsType g : spec.getGoodsTypeList()) {
        if (!g.isStorable())
            continue;
        int oldCount = colony.getGoodsCount(g);
        int newCount = scratch.getGoodsCount(g);
        if (newCount != oldCount) {
            colony.getGoodsContainer().addGoods(g, newCount - oldCount);
        }
    }
    scratch.disposeScratchColony();
    // Emergency recovery if something broke and the colony is empty. 
    if (colony.getWorkLocationUnitCount() <= 0) {
        String destruct = "Autodestruct at " + colony.getName() + " in " + turn + ":";
        for (UnitWas uw : was) destruct += "\n" + uw.toString();
        logger.warning(destruct);
        avertAutoDestruction();
    }
    // Argh.  We may have chosen to build something we can no 
    // longer build due to some limitation.  Try to find a 
    // replacement, but do not re-refine/assign as that process is 
    // sufficiently complex that we can not be confident that this 
    // will not loop indefinitely.  The compromise is to just 
    // rearrange next turn until we get out of this state. 
    if (build != null && !colony.canBuild(build)) {
        logger.warning(colony.getName() + " reneged building " + Utils.lastPart(build.toString(), ".") + ": " + colony.getNoBuildReason(build));
        List<BuildableType> queue = new ArrayList<BuildableType>();
        build = colonyPlan.getBestBuildableType();
        if (build != null)
            queue.add(build);
        AIMessage.askSetBuildQueue(this, queue);
        nextRearrange = 1;
    }
    // Now that all production has been stabilized, plan to 
    // rearrange when the warehouse hits a limit. 
    if (colony.getNetProductionOf(spec.getPrimaryFoodType()) < 0) {
        int net = colony.getNetProductionOf(spec.getPrimaryFoodType());
        int when = colony.getGoodsCount(spec.getPrimaryFoodType()) / -net;
        nextRearrange = Math.max(0, Math.min(nextRearrange, when - 1));
    }
    int warehouse = colony.getWarehouseCapacity();
    for (GoodsType g : spec.getGoodsTypeList()) {
        if (!g.isStorable() || g.isFoodType())
            continue;
        int have = colony.getGoodsCount(g);
        int net = colony.getAdjustedNetProductionOf(g);
        if (net >= 0 && (have >= warehouse || g.limitIgnored()))
            continue;
        int when = (net < 0) ? (have / -net - 1) : (net > 0) ? ((warehouse - have) / net - 1) : Integer.MAX_VALUE;
        nextRearrange = Math.max(1, Math.min(nextRearrange, when));
    }
    // Log the changes. 
    build = colony.getCurrentlyBuilding();
    String buildStr = (build != null) ? build.toString() : ((build = colonyPlan.getBestBuildableType()) != null) ? "unexpected-null(" + build.toString() + ")" : "expected-null";
    String report = "Rearrange " + colony.getName() + " (" + colony.getWorkLocationUnitCount() + ")" + " build=" + buildStr + " " + getGame().getTurn() + " + " + nextRearrange;
    for (UnitWas uw : was) report += "\n" + uw.toString();
    logger.finest(report);
    // Give suitable missions to all units. 
    for (Unit u : colony.getUnitList()) {
        AIUnit aiU = getAIUnit(u);
        if (aiU.getMission() instanceof WorkInsideColonyMission && ((WorkInsideColonyMission) aiU.getMission()).getAIColony() == this) {
            ;
        } else {
            aiU.setMission(new WorkInsideColonyMission(aiMain, aiU, this));
        }
    }
    EuropeanAIPlayer aip = (EuropeanAIPlayer) aiMain.getAIPlayer(player);
    boolean pioneersWanted = aip.pioneersNeeded() > 0;
    Tile pioneerTile;
    for (Unit u : tile.getUnitList()) {
        AIUnit aiU = getAIUnit(u);
        if (aiU == null || aiU.getMission() != null)
            continue;
        Mission m = null;
        switch(u.getRole()) {
            case SOLDIER:
            case DRAGOON:
                m = new DefendSettlementMission(aiMain, aiU, colony);
                break;
            case SCOUT:
                if (preferScouts)
                    m = aip.getScoutingMission(aiU);
                break;
            case PIONEER:
                if (pioneersWanted)
                    m = aip.getPioneeringMission(aiU);
                break;
            case MISSIONARY:
                m = aip.getMissionaryMission(aiU);
                break;
            default:
                break;
        }
        if (m != null)
            aiU.setMission(m);
    }
    // Change the export settings when required. 
    resetExports();
    createTileImprovementPlans();
    updateWishes();
    // Set the next rearrangement turn. 
    rearrangeTurn = new Turn(turn + nextRearrange);
    return true;
}
