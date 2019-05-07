/**
     * Returns the maximum food consumption of any unit types
     * available to this player.
     *
     * @return an <code>int</code> value
     */
public int getMaximumFoodConsumption() {
    if (maximumFoodConsumption < 0) {
        for (UnitType unitType : getSpecification().getUnitTypeList()) {
            if (unitType.isAvailableTo(this)) {
                int foodConsumption = 0;
                for (GoodsType foodType : getSpecification().getFoodGoodsTypeList()) {
                    foodConsumption += unitType.getConsumptionOf(foodType);
                }
                if (foodConsumption > maximumFoodConsumption) {
                    maximumFoodConsumption = foodConsumption;
                }
            }
        }
    }
    return maximumFoodConsumption;
}
