/**
     * Gets the non-food-producing/non-autoproducing work location
     * plans associated with this <code>ColonyPlan</code>.
     *
     * @return A list of nonfood producing plans.
     */
public List<WorkLocationPlan> getWorkPlans() {
    List<WorkLocationPlan> plans = new ArrayList<WorkLocationPlan>();
    for (WorkLocationPlan wlp : workPlans) {
        if (!wlp.getGoodsType().isFoodType() && !wlp.getWorkLocation().canAutoProduce())
            plans.add(wlp);
    }
    return plans;
}
