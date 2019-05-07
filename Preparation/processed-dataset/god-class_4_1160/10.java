/**
     * Gets the goods required to complete a build.  The list includes
     * the prerequisite raw materials as well as the direct
     * requirements (i.e. hammers, tools).  If enough of a required
     * goods is present in the colony, then that type is not returned.
     * Take care to order types with raw materials first so that we
     * can prioritize gathering what is required before manufacturing.
     *
     * Public for the benefit of the test suite.
     *
     * @param buildable The <code>BuildableType</code> to consider.
     * @return A list of required abstract goods.
     */
public List<AbstractGoods> getRequiredGoods(BuildableType buildable) {
    List<AbstractGoods> required = new ArrayList<AbstractGoods>();
    if (buildable == null)
        return required;
    for (AbstractGoods ag : buildable.getRequiredGoods()) {
        int amount = ag.getAmount();
        GoodsType type = ag.getType();
        while (type != null) {
            if (amount <= colony.getGoodsCount(type))
                break;
            // Shortcut 
            required.add(0, new AbstractGoods(type, amount - colony.getGoodsCount(type)));
            type = type.getRawMaterial();
        }
    }
    return required;
}
