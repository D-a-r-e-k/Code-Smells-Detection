/**
     * Recreates the buildables and work location plans for this
     * colony.
     */
public void update() {
    UnitType defaultUnitType = spec().getDefaultUnitType();
    // Update the profile type. 
    profileType = ProfileType.getProfileTypeFromSize(colony.getWorkLocationUnitCount());
    // Build the total map of all possible production with standard units. 
    Map<GoodsType, Map<WorkLocation, Integer>> production = createProductionMap();
    // Set the goods type lists, and prune production of manufactured 
    // goods that are missing raw materials and other non-interesting. 
    updateGoodsTypeLists(production);
    // Set the preferred raw materials.  Prune production and 
    // goods lists further removing the non-preferred new world 
    // raw and refined materials. 
    updateRawMaterials(production);
    // The buildables depend on the profile type, the goods type lists 
    // and/or goods-to-produce list. 
    updateBuildableTypes();
    // Make plans for each valid <goods, location> production and 
    // complete the list of goods to produce. 
    updatePlans(production);
}
