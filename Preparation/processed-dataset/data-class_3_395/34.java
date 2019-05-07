/**
   * Gets the average cost, that is, total cost of misclassifications (incorrect
   * plus unclassified) over the total number of instances.
   * 
   * @return the average cost.
   */
public final double avgCost() {
    return m_delegate.avgCost();
}
