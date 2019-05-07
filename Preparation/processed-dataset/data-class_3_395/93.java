/**
   * Tests whether the current evaluation object is equal to another evaluation
   * object.
   * 
   * @param obj the object to compare against
   * @return true if the two objects are equal
   */
@Override
public boolean equals(Object obj) {
    if (obj instanceof weka.classifiers.Evaluation) {
        obj = ((weka.classifiers.Evaluation) obj).m_delegate;
    }
    return m_delegate.equals(obj);
}
