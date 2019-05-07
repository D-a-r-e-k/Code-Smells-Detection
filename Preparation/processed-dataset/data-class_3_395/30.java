/**
   * Gets the average size of the predicted regions, relative to the range of
   * the target in the training data, at the confidence level specified when
   * evaluation was performed.
   * 
   * @return the average size of the predicted regions
   */
public final double sizeOfPredictedRegions() {
    return m_delegate.sizeOfPredictedRegions();
}
