/**
     * Adds an internal entity declaration.
     * <p>
     * <strong>Note:</strong> This method ignores subsequent entity
     * declarations.
     * <p>
     * <strong>Note:</strong> The name should be a unique symbol. The
     * SymbolTable can be used for this purpose.
     *
     * @param name The name of the entity.
     * @param text The text of the entity.
     *
     * @see SymbolTable
     */
public void addInternalEntity(String name, String text) {
    if (!fEntities.containsKey(name)) {
        Entity entity = new InternalEntity(name, text, fInExternalSubset);
        fEntities.put(name, entity);
    } else {
        if (fWarnDuplicateEntityDef) {
            fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "MSG_DUPLICATE_ENTITY_DEFINITION", new Object[] { name }, XMLErrorReporter.SEVERITY_WARNING);
        }
    }
}
