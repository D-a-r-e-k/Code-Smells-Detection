public void readFromXML(XMLStreamReader xsr) throws XMLStreamException {
    String newId = xsr.getAttributeValue(null, FreeColObject.ID_ATTRIBUTE_TAG);
    if (difficultyLevel == null) {
        difficultyLevel = xsr.getAttributeValue(null, "difficultyLevel");
    }
    logger.fine("Difficulty level is " + difficultyLevel);
    if (id == null) {
        // don't overwrite id with parent id! 
        id = newId;
    }
    logger.fine("Reading specification " + newId);
    String parentId = xsr.getAttributeValue(null, FreeColGameObjectType.EXTENDS_TAG);
    if (parentId != null) {
        try {
            FreeColTcFile parent = new FreeColTcFile(parentId);
            load(parent.getSpecificationInputStream());
            initialized = false;
        } catch (IOException e) {
            throw new XMLStreamException("Failed to open parent specification: " + e);
        }
    }
    while (xsr.nextTag() != XMLStreamConstants.END_ELEMENT) {
        String childName = xsr.getLocalName();
        logger.finest("Found child named " + childName);
        ChildReader reader = readerMap.get(childName);
        if (reader == null) {
            // @compat 0.9.x 
            if ("improvementaction-types".equals(childName)) {
                while (xsr.nextTag() != XMLStreamConstants.END_ELEMENT) {
                    // skip children 
                    while ("action".equals(xsr.getLocalName())) {
                        xsr.nextTag();
                    }
                }
            } else {
                throw new RuntimeException("unexpected: " + childName);
            }
        } else {
            reader.readChildren(xsr);
        }
    }
    if (difficultyLevel != null) {
        applyDifficultyLevel(difficultyLevel);
    }
    // @compat 0.9.x 
    for (BuildingType bt : getBuildingTypeList()) {
        bt.fixup09x();
    }
    if (getModifiers("model.modifier.nativeTreasureModifier") != null) {
        for (FoundingFather ff : getFoundingFathers()) {
            ff.fixup09x();
        }
    }
    // end compatibility code 
    // @compat 0.10.1 
    String[] years = new String[] { "startingYear", "seasonYear", "mandatoryColonyYear", "lastYear", "lastColonialYear" };
    int[] values = new int[] { 1492, 1600, 1600, 1850, 1800 };
    for (int index = 0; index < years.length; index++) {
        String id = "model.option." + years[index];
        if (allOptions.get(id) == null) {
            IntegerOption option = new IntegerOption(id);
            option.setValue(values[index]);
            allOptions.put(id, option);
        }
    }
    // end compatibility code 
    // @compat 0.10.5 
    String id = "model.option.interventionBells";
    if (allOptions.get(id) == null) {
        IntegerOption interventionBells = new IntegerOption(id);
        interventionBells.setValue(5000);
        allOptions.put(id, interventionBells);
    }
    id = "model.option.interventionTurns";
    if (allOptions.get(id) == null) {
        IntegerOption interventionTurns = new IntegerOption(id);
        interventionTurns.setValue(52);
        allOptions.put(id, interventionTurns);
    }
    id = "model.option.interventionForce";
    if (allOptions.get(id) == null) {
        UnitListOption interventionForce = new UnitListOption(id);
        AbstractUnitOption regulars = new AbstractUnitOption(id + ".regulars");
        regulars.setValue(new AbstractUnit("model.unit.colonialRegular", Unit.Role.SOLDIER, 2));
        interventionForce.getValue().add(regulars);
        AbstractUnitOption dragoons = new AbstractUnitOption(id + ".dragoons");
        dragoons.setValue(new AbstractUnit("model.unit.colonialRegular", Unit.Role.DRAGOON, 2));
        interventionForce.getValue().add(dragoons);
        AbstractUnitOption artillery = new AbstractUnitOption(id + ".artillery");
        artillery.setValue(new AbstractUnit("model.unit.artillery", Unit.Role.DEFAULT, 2));
        interventionForce.getValue().add(artillery);
        AbstractUnitOption menOfWar = new AbstractUnitOption(id + ".menOfWar");
        menOfWar.setValue(new AbstractUnit("model.unit.manOWar", Unit.Role.DEFAULT, 2));
        interventionForce.getValue().add(menOfWar);
        allOptions.put(id, interventionForce);
    }
    id = "model.option.mercenaryForce";
    if (allOptions.get(id) == null) {
        UnitListOption mercenaryForce = new UnitListOption(id);
        AbstractUnitOption regulars = new AbstractUnitOption(id + ".regulars");
        regulars.setValue(new AbstractUnit("model.unit.veteranSoldier", Unit.Role.SOLDIER, 2));
        mercenaryForce.getValue().add(regulars);
        AbstractUnitOption dragoons = new AbstractUnitOption(id + ".dragoons");
        dragoons.setValue(new AbstractUnit("model.unit.veteranSoldier", Unit.Role.DRAGOON, 2));
        mercenaryForce.getValue().add(dragoons);
        AbstractUnitOption artillery = new AbstractUnitOption(id + ".artillery");
        artillery.setValue(new AbstractUnit("model.unit.artillery", Unit.Role.DEFAULT, 2));
        mercenaryForce.getValue().add(artillery);
        AbstractUnitOption menOfWar = new AbstractUnitOption(id + ".menOfWar");
        menOfWar.setValue(new AbstractUnit("model.unit.manOWar", Unit.Role.DEFAULT, 2));
        mercenaryForce.getValue().add(menOfWar);
        allOptions.put(id, mercenaryForce);
    }
    id = "model.option.goodGovernmentLimit";
    if (allOptions.get(id) == null) {
        IntegerOption goodGovernmentLimit = new IntegerOption(id);
        goodGovernmentLimit.setValue(50);
        allOptions.put(id, goodGovernmentLimit);
    }
    id = "model.option.veryGoodGovernmentLimit";
    if (allOptions.get(id) == null) {
        IntegerOption veryGoodGovernmentLimit = new IntegerOption(id);
        veryGoodGovernmentLimit.setValue(100);
        allOptions.put(id, veryGoodGovernmentLimit);
    }
    EquipmentType missionaryEquipment = getEquipmentType("model.equipment.missionary");
    if (missionaryEquipment != null) {
        for (String as : new String[] { "model.ability.establishMission", "model.ability.denounceHeresy", "model.ability.inciteNatives" }) {
            List<Ability> al = allAbilities.get(as);
            if (al != null) {
                for (Ability a : al) missionaryEquipment.addAbility(a);
            }
        }
    }
    // end compatibility code 
    initialized = true;
}
