/**
   * Set the error event listener in effect for the transformation.
   *
   * @param listener The new error listener.
   * @throws IllegalArgumentException if listener is null.
   */
public void setErrorListener(ErrorListener listener) throws IllegalArgumentException {
    if (listener == null)
        throw new IllegalArgumentException(XSLMessages.createMessage(XSLTErrorResources.ER_NULL_ERROR_HANDLER, null));
    else
        m_errorListener = listener;
}
