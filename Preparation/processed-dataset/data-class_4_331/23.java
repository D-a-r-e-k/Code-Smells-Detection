/**
     * Finds a plan on a list that produces a given goods type.
     *
     * @param goodsType The <code>GoodsType</code> to produce.
     * @param plans The list of <code>WorkLocationPlan</code>s to check.
     * @return The first plan found that produces the goods type, or null
     *     if none found.
     */
private WorkLocationPlan findPlan(GoodsType goodsType, List<WorkLocationPlan> plans) {
    for (WorkLocationPlan wlp : plans) {
        if (wlp.getGoodsType() == goodsType)
            return wlp;
    }
    return null;
}
