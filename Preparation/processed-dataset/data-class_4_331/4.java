// Public functionality. 
/**
     * Gets the preferred goods to produce.
     *
     * @return A copy of the preferred goods production list in this plan.
     */
public List<GoodsType> getPreferredProduction() {
    return new ArrayList<GoodsType>(produce);
}
