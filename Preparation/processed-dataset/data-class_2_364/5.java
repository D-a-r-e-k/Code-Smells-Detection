/**
     * @see org.exolab.castor.xml.OutputFormat#setDoctype(java.lang.String, java.lang.String)
     * {@inheritDoc}
     */
public void setDoctype(final String type1, final String type2) {
    Method method;
    try {
        method = _outputFormat.getClass().getMethod("setDoctype", new Class[] { String.class, String.class });
        method.invoke(_outputFormat, new Object[] { type1, type2 });
    } catch (Exception e) {
        String msg = "Problem invoking OutputFormat.setDoctype()";
        LOG.error(msg, e);
        throw new RuntimeException(msg + e.getMessage());
    }
}
