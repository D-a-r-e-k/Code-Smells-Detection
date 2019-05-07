/**
   * Outputs the performance statistics as a classification confusion matrix.
   * For each class value, shows the distribution of predicted class values.
   * 
   * @param title the title for the confusion matrix
   * @return the confusion matrix as a String
   * @throws Exception if the class is numeric
   */
public String toMatrixString(String title) throws Exception {
    return m_delegate.toMatrixString(title);
}
