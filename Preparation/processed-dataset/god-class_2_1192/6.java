/**
     * @see org.exolab.castor.xml.OutputFormat#setOmitXMLDeclaration(boolean)
     * {@inheritDoc}
     */
public void setOmitXMLDeclaration(final boolean omitXMLDeclaration) {
    Method method;
    try {
        method = _outputFormat.getClass().getMethod("setOmitXMLDeclaration", new Class[] { boolean.class });
        method.invoke(_outputFormat, new Object[] { Boolean.valueOf(omitXMLDeclaration) });
    } catch (Exception e) {
        String msg = "Problem invoking OutputFormat.setOmitXMLDeclaration()";
        LOG.error(msg, e);
        throw new RuntimeException(msg + e.getMessage());
    }
}
