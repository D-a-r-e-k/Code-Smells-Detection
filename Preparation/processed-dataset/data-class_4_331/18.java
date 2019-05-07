/**
     * Updates the build plans for this colony.
     */
private void updateBuildableTypes() {
    final EuropeanAIPlayer euaip = (EuropeanAIPlayer) getAIMain().getAIPlayer(colony.getOwner());
    String advantage = euaip.getAIAdvantage();
    buildPlans.clear();
    int maxLevel;
    switch(profileType) {
        case OUTPOST:
        case SMALL:
            maxLevel = 1;
            break;
        case MEDIUM:
            maxLevel = 2;
            break;
        case LARGE:
            maxLevel = 3;
            break;
        case CAPITAL:
            maxLevel = 4;
            break;
        default:
            throw new IllegalStateException("Bogus profile type: " + profileType);
    }
    Player player = colony.getOwner();
    for (BuildingType type : spec().getBuildingTypeList()) {
        boolean expectFail = false;
        if (!colony.canBuild(type))
            continue;
        // Exempt defence and export from the level check. 
        if (!type.getModifierSet(Modifier.DEFENCE).isEmpty()) {
            double factor = 1.0;
            if ("conquest".equals(advantage))
                factor = 1.1;
            prioritize(type, FORTIFY_WEIGHT * factor, 1.0);
        }
        if (type.hasAbility(Ability.EXPORT)) {
            double factor = 1.0;
            if ("trade".equals(advantage))
                factor = 1.1;
            prioritize(type, EXPORT_WEIGHT * factor, 1.0);
        }
        // Skip later stage buildings for smaller settlements. 
        if (type.getLevel() > maxLevel)
            continue;
        // Scale docks by the improvement available to the food supply. 
        if (type.hasAbility(Ability.PRODUCE_IN_WATER)) {
            double factor = 0.0;
            if (!colony.hasAbility(Ability.PRODUCE_IN_WATER) && colony.getTile().isShore()) {
                int landFood = 0, seaFood = 0;
                for (Tile t : colony.getTile().getSurroundingTiles(1)) {
                    if (t.getOwningSettlement() == colony || player.canClaimForSettlement(t)) {
                        for (AbstractGoods ag : t.getSortedPotential()) {
                            if (ag.getType().isFoodType()) {
                                if (t.isLand()) {
                                    landFood += ag.getAmount();
                                } else {
                                    seaFood += ag.getAmount();
                                }
                            }
                        }
                    }
                }
                factor = (seaFood + landFood == 0) ? 0.0 : seaFood / (double) (seaFood + landFood);
            }
            prioritize(type, FISH_WEIGHT, factor);
        }
        if (type.hasAbility(Ability.BUILD)) {
            double factor = 1.0;
            if ("building".equals(advantage))
                factor = 1.1;
            double support = 1.0;
            for (Ability a : type.getAbilitySet(Ability.BUILD)) {
                List<Scope> scopes = a.getScopes();
                if (scopes != null && !scopes.isEmpty())
                    support = 0.1;
            }
            prioritize(type, BUILDING_WEIGHT * factor, support);
        }
        if (type.hasAbility(Ability.CAN_TEACH)) {
            prioritize(type, TEACH_WEIGHT, 1.0);
        }
        if (type.hasAbility(Ability.REPAIR_UNITS)) {
            double factor = 1.0;
            if ("naval".equals(advantage))
                factor = 1.1;
            prioritize(type, REPAIR_WEIGHT * factor, 1.0);
        }
        GoodsType output = type.getProducedGoodsType();
        if (output != null) {
            if (!prioritizeProduction(type, output)) {
                // Allow failure if this building can not build. 
                expectFail = true;
            }
        } else {
            for (GoodsType g : spec().getGoodsTypeList()) {
                if (!type.getModifierSet(g.getId()).isEmpty()) {
                    if (!prioritizeProduction(type, g)) {
                        expectFail = true;
                    }
                }
            }
            // Hacks.  No good way to make this really generic. 
            if (!type.getModifierSet("model.modifier.warehouseStorage").isEmpty()) {
                double factor = 1.0;
                if ("trade".equals(advantage))
                    factor = 1.1;
                prioritize(type, STORAGE_WEIGHT * factor, 1.0);
            }
            if (!type.getModifierSet("model.modifier.breedingDivisor").isEmpty()) {
                prioritize(type, BREEDING_WEIGHT, 1.0);
            }
        }
        if (findBuildPlan(type) == null && !expectFail) {
            logger.warning("No building priority found for: " + type);
        }
    }
    double wagonNeed = 0.0;
    if (!colony.isConnectedPort()) {
        // Inland colonies need transportation 
        int wagons = euaip.getNeededWagons(colony.getTile());
        wagonNeed = (wagons <= 0) ? 0.0 : (wagons > 3) ? 1.0 : wagons / 3.0;
    }
    for (UnitType unitType : spec().getUnitTypeList()) {
        if (!colony.canBuild(unitType))
            continue;
        if (unitType.hasAbility(Ability.NAVAL_UNIT)) {
            ;
        } else if (unitType.isDefensive()) {
            if (AIColony.isBadlyDefended(colony)) {
                prioritize(unitType, DEFENCE_WEIGHT, 1.0);
            }
        } else if (unitType.hasAbility(Ability.CARRY_GOODS)) {
            if (wagonNeed > 0.0) {
                double factor = 1.0;
                if ("trade".equals(advantage))
                    factor = 1.1;
                prioritize(unitType, TRANSPORT_WEIGHT * factor, wagonNeed);
            }
        }
    }
    // Weight by lower required goods. 
    for (BuildPlan bp : buildPlans) {
        double difficulty = 0.0f;
        for (AbstractGoods ag : bp.type.getRequiredGoods()) {
            GoodsType g = ag.getType();
            int need = ag.getAmount() - colony.getGoodsCount(g);
            if (need > 0) {
                // Penalize building with type that can not be 
                // made locally. 
                double f = (produce.contains(g.getRawMaterial())) ? 1.0 : 5.0;
                difficulty += need * f;
            }
        }
        bp.difficulty = Math.max(1.0f, Math.sqrt(difficulty));
    }
    Collections.sort(buildPlans, buildPlanComparator);
}
