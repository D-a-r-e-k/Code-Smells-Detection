/**
     * @see org.exolab.castor.xml.OutputFormat#setIndenting(boolean)
     * {@inheritDoc}
     */
public void setIndenting(final boolean indent) {
    Method method;
    try {
        method = _outputFormat.getClass().getMethod("setIndenting", new Class[] { boolean.class });
        method.invoke(_outputFormat, new Object[] { Boolean.valueOf(indent) });
    } catch (Exception e) {
        String msg = "Problem invoking OutputFormat.setIndenting()";
        LOG.error(msg, e);
        throw new RuntimeException(msg + e.getMessage());
    }
}
