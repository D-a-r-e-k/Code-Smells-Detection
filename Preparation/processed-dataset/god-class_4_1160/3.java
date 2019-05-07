/**
     * Gets the production for a work location of a specified goods type,
     * using the default unit type to avoid considering which unit is
     * to be allocated.  This is thus an approximation to what will
     * finally occur when units are assigned, but it serves for planning
     * purposes.
     *
     * @param wl The <code>WorkLocation</code> where production is to occur.
     * @param goodsType The <code>GoodsType</code> to produce.
     * @return The work location production.
     */
private int getWorkLocationProduction(WorkLocation wl, GoodsType goodsType) {
    return wl.getPotentialProduction(goodsType, spec().getDefaultUnitType());
}
