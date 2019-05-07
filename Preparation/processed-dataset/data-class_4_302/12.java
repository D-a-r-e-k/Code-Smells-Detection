/**
     * Adds an unparsed entity declaration.
     * <p>
     * <strong>Note:</strong> This method ignores subsequent entity
     * declarations.
     * <p>
     * <strong>Note:</strong> The name should be a unique symbol. The
     * SymbolTable can be used for this purpose.
     *
     * @param name     The name of the entity.
     * @param publicId The public identifier of the entity.
     * @param systemId The system identifier of the entity.
     * @param notation The name of the notation.
     *
     * @see SymbolTable
     */
public void addUnparsedEntity(String name, String publicId, String systemId, String baseSystemId, String notation) {
    if (!fEntities.containsKey(name)) {
        Entity entity = new ExternalEntity(name, new XMLEntityDescriptionImpl(name, publicId, systemId, baseSystemId, null), notation, fInExternalSubset);
        fEntities.put(name, entity);
    } else {
        if (fWarnDuplicateEntityDef) {
            fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "MSG_DUPLICATE_ENTITY_DEFINITION", new Object[] { name }, XMLErrorReporter.SEVERITY_WARNING);
        }
    }
}
