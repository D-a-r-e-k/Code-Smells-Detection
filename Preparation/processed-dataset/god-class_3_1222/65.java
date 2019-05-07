/**
   * Generates a breakdown of the accuracy for each class (with default title),
   * incorporating various information-retrieval statistics, such as true/false
   * positive rate, precision/recall/F-Measure. Should be useful for ROC curves,
   * recall/precision curves.
   * 
   * @return the statistics presented as a string
   * @throws Exception if class is not nominal
   */
public String toClassDetailsString() throws Exception {
    return m_delegate.toClassDetailsString();
}
