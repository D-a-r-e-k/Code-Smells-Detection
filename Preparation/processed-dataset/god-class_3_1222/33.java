/**
   * Gets the total cost, that is, the cost of each prediction times the weight
   * of the instance, summed over all instances.
   * 
   * @return the total cost
   */
public final double totalCost() {
    return m_delegate.totalCost();
}
