public void clean() {
    logger.finest("Cleaning up specification.");
    Iterator<FreeColGameObjectType> typeIterator = allTypes.values().iterator();
    while (typeIterator.hasNext()) {
        FreeColGameObjectType type = typeIterator.next();
        if (type.isAbstractType()) {
            typeIterator.remove();
        }
    }
    for (Nation nation : nations) {
        if (nation.getType().isEuropean()) {
            if (nation.getType().isREF()) {
                REFNations.add(nation);
            } else {
                europeanNations.add(nation);
            }
        } else {
            indianNations.add(nation);
        }
    }
    nationTypes.addAll(indianNationTypes);
    nationTypes.addAll(europeanNationTypes);
    Iterator<EuropeanNationType> iterator = europeanNationTypes.iterator();
    while (iterator.hasNext()) {
        EuropeanNationType nationType = iterator.next();
        if (nationType.isREF()) {
            REFNationTypes.add(nationType);
            iterator.remove();
        }
    }
    for (UnitType unitType : unitTypeList) {
        if (unitType.getExpertProduction() != null) {
            experts.put(unitType.getExpertProduction(), unitType);
        }
        if (unitType.hasPrice()) {
            if (unitType.getSkill() > 0) {
                unitTypesTrainedInEurope.add(unitType);
            } else if (!unitType.hasSkill()) {
                unitTypesPurchasedInEurope.add(unitType);
            }
        }
    }
    for (GoodsType goodsType : goodsTypeList) {
        if (goodsType.isFarmed()) {
            farmedGoodsTypeList.add(goodsType);
        }
        if (goodsType.isFoodType()) {
            foodGoodsTypeList.add(goodsType);
        }
        if (goodsType.isNewWorldGoodsType()) {
            newWorldGoodsTypeList.add(goodsType);
        }
        if (goodsType.isLibertyType()) {
            libertyGoodsTypeList.add(goodsType);
        }
        if (goodsType.isImmigrationType()) {
            immigrationGoodsTypeList.add(goodsType);
        }
        if (goodsType.isRawBuildingMaterial() && !goodsType.isFoodType()) {
            rawBuildingGoodsTypeList.add(goodsType);
        }
        if (goodsType.isStorable()) {
            storableTypes++;
        }
    }
    // now that specification is complete, dynamically generate 
    // option choices 
    for (AbstractOption option : allOptions.values()) {
        option.generateChoices();
    }
    if (difficultyLevel != null) {
        applyDifficultyLevel(difficultyLevel);
    }
    // Initialize the Turn class using GameOptions. 
    try {
        int startingYear = getInteger(GameOptions.STARTING_YEAR);
        int seasonYear = getInteger(GameOptions.SEASON_YEAR);
        if (seasonYear < startingYear)
            seasonYear = startingYear;
        Turn.setStartingYear(startingYear);
        Turn.setSeasonYear(seasonYear);
        logger.info("Initialized turn" + ", starting year=" + Integer.toString(Turn.getStartingYear()) + ", season year=" + Integer.toString(Turn.getSeasonYear()));
    } catch (Exception e) {
        logger.log(Level.WARNING, "Failed to set year options", e);
    }
    // generate dynamic game options 
    OptionGroup prices = new OptionGroup("gameOptions.prices", this);
    for (GoodsType goodsType : goodsTypeList) {
        String name = goodsType.getSuffix("model.goods.");
        if (goodsType.getInitialSellPrice() > 0) {
            int diff = (goodsType.isNewWorldGoodsType() || goodsType.isNewWorldLuxuryType()) ? 3 : 0;
            IntegerOption minimum = new IntegerOption("model.option." + name + ".minimumPrice", this);
            minimum.setValue(goodsType.getInitialSellPrice());
            minimum.setMinimumValue(1);
            minimum.setMaximumValue(100);
            prices.add(minimum);
            addAbstractOption(minimum);
            IntegerOption maximum = new IntegerOption("model.option." + name + ".maximumPrice", this);
            maximum.setValue(goodsType.getInitialSellPrice() + diff);
            maximum.setMinimumValue(1);
            maximum.setMaximumValue(100);
            prices.add(maximum);
            addAbstractOption(maximum);
            IntegerOption spread = new IntegerOption("model.option." + name + ".spread", this);
            spread.setValue(goodsType.getPriceDifference());
            spread.setMinimumValue(1);
            spread.setMaximumValue(100);
            prices.add(spread);
            addAbstractOption(spread);
        } else if (goodsType.getPrice() < FreeColGameObjectType.INFINITY) {
            IntegerOption price = new IntegerOption("model.option." + name + ".price", this);
            price.setValue(goodsType.getPrice());
            price.setMinimumValue(1);
            price.setMaximumValue(100);
            prices.add(price);
            addAbstractOption(price);
        }
    }
    getOptionGroup("gameOptions").add(prices);
    allOptionGroups.put(id, prices);
    logger.info("Specification initialization complete. " + allTypes.size() + " FreeColGameObjectTypes,\n" + allOptions.size() + " Options, " + allAbilities.size() + " Abilities, " + allModifiers.size() + " Modifiers read.");
}
