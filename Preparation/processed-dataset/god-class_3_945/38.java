/**
     * Return the size of a sql type in case it is a string type. Otherwise return null.
     *
     * @return get the size.
     */
public String getSize() {
    TemplateString theType = getSqlType();
    if (theType == null) {
        return null;
    }
    List parameters = StrutsValidation.getParams(getSqlType().toString());
    if (getSqlType().toString().indexOf("CHAR") != -1 && !parameters.isEmpty()) {
        return (String) parameters.get(0);
    }
    return null;
}
