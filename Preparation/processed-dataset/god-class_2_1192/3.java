/**
     * @see org.exolab.castor.xml.OutputFormat#setPreserveSpace(boolean)
     * {@inheritDoc}
     */
public void setPreserveSpace(final boolean preserveSpace) {
    Method method;
    try {
        method = _outputFormat.getClass().getMethod("setPreserveSpace", new Class[] { boolean.class });
        method.invoke(_outputFormat, new Object[] { Boolean.valueOf(preserveSpace) });
    } catch (Exception e) {
        String msg = "Problem invoking OutputFormat.setPreserveSpace()";
        LOG.error(msg, e);
        throw new RuntimeException(msg + e.getMessage());
    }
}
