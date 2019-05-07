/**
     * @see org.exolab.castor.xml.OutputFormat#setMethod(java.lang.String)
     * {@inheritDoc}
     */
public void setMethod(final String method) {
    Method aMethod;
    try {
        aMethod = _outputFormat.getClass().getMethod("setMethod", new Class[] { String.class });
        aMethod.invoke(_outputFormat, new Object[] { method });
    } catch (Exception e) {
        String msg = "Problem invoking OutputFormat.setMethod()";
        LOG.error(msg, e);
        throw new RuntimeException(msg + e.getMessage());
    }
}
