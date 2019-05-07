/**
   * Calculates the matthews correlation coefficient (sometimes called phi
   * coefficient) for the supplied class
   * 
   * @param classIndex the index of the class to compute the matthews
   *          correlation coefficient for
   * 
   * @return the mathews correlation coefficient
   */
public double matthewsCorrelationCoefficient(int classIndex) {
    return m_delegate.matthewsCorrelationCoefficient(classIndex);
}
