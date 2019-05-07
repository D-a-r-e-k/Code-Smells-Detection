/**
   * Generates a breakdown of the accuracy for each class, incorporating various
   * information-retrieval statistics, such as true/false positive rate,
   * precision/recall/F-Measure. Should be useful for ROC curves,
   * recall/precision curves.
   * 
   * @param title the title to prepend the stats string with
   * @return the statistics presented as a string
   * @throws Exception if class is not nominal
   */
public String toClassDetailsString(String title) throws Exception {
    return m_delegate.toClassDetailsString(title);
}
