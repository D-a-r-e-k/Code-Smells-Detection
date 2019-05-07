/**
   * Add a new data value to the current estimator.
   *
   * @param data the new data value 
   * @param given the new value that data is conditional upon 
   * @param weight the weight assigned to the data value 
   */
public void addValue(double data, double given, double weight) {
    m_Estimators[(int) given].addValue(data, weight);
}
