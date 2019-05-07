/**
     * Gets the food-producing and non-autoproducing work location
     * plans associated with this <code>ColonyPlan</code>.
     *
     * @return A list of food producing plans.
     */
public List<WorkLocationPlan> getFoodPlans() {
    List<WorkLocationPlan> plans = new ArrayList<WorkLocationPlan>();
    for (WorkLocationPlan wlp : workPlans) {
        if (wlp.getGoodsType().isFoodType() && !wlp.getWorkLocation().canAutoProduce())
            plans.add(wlp);
    }
    return plans;
}
