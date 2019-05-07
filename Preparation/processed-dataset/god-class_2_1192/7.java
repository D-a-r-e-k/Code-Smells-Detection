/**
     * @see org.exolab.castor.xml.OutputFormat#setOmitDocumentType(boolean)
     * {@inheritDoc}
     */
public void setOmitDocumentType(final boolean omitDocumentType) {
    Method method;
    try {
        method = _outputFormat.getClass().getMethod("setOmitDocumentType", new Class[] { boolean.class });
        method.invoke(_outputFormat, new Object[] { Boolean.valueOf(omitDocumentType) });
    } catch (Exception e) {
        String msg = "Problem invoking OutputFormat.setOmitDocumentType()";
        LOG.error(msg, e);
        throw new RuntimeException(msg + e.getMessage());
    }
}
