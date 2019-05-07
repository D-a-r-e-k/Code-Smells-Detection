/**
   * Returns the mean absolute error. Refers to the error of the predicted
   * values for numeric classes, and the error of the predicted probability
   * distribution for nominal classes.
   * 
   * @return the mean absolute error
   */
public final double meanAbsoluteError() {
    return m_delegate.meanAbsoluteError();
}
