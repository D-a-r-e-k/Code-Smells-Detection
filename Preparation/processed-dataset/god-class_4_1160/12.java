/**
     * Creates a map of potential production of all goods types
     * from all available work locations using the default unit type.
     * Includes non-workable locations (e.g. chapel, colony-center-tile)
     * as their production can influence the choice of goods to produce.
     *
     * @return The map of potential production.
     */
private Map<GoodsType, Map<WorkLocation, Integer>> createProductionMap() {
    Map<GoodsType, Map<WorkLocation, Integer>> production = new HashMap<GoodsType, Map<WorkLocation, Integer>>();
    for (WorkLocation wl : colony.getAvailableWorkLocations()) {
        for (GoodsType g : spec().getGoodsTypeList()) {
            int p = getWorkLocationProduction(wl, g);
            if (p > 0) {
                Map<WorkLocation, Integer> m = production.get(g);
                if (m == null) {
                    m = new HashMap<WorkLocation, Integer>();
                    production.put(g, m);
                }
                m.put(wl, new Integer(p));
            }
        }
    }
    return production;
}
