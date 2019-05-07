/**
   * Enables the user of the TransformerHandler to set the
   * to set the Result for the transformation.
   *
   * @param result A Result instance, should not be null.
   *
   * @throws IllegalArgumentException if result is invalid for some reason.
   */
public void setResult(Result result) throws IllegalArgumentException {
    if (null == result)
        throw new IllegalArgumentException(XSLMessages.createMessage(XSLTErrorResources.ER_RESULT_NULL, null));
    //"Result should not be null");          
    m_result = result;
}
