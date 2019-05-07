/**
     * @see org.exolab.castor.xml.OutputFormat#setEncoding(java.lang.String)
     * {@inheritDoc}
     */
public void setEncoding(final String encoding) {
    Method method;
    try {
        method = _outputFormat.getClass().getMethod("setEncoding", new Class[] { String.class });
        method.invoke(_outputFormat, new Object[] { encoding });
    } catch (Exception e) {
        String msg = "Problem invoking OutputFormat.setEncoding()";
        LOG.error(msg, e);
        throw new RuntimeException(msg + e.getMessage());
    }
}
