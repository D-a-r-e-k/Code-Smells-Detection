// addInternalEntity(String,String)  
/**
     * Adds an external entity declaration.
     * <p>
     * <strong>Note:</strong> This method ignores subsequent entity
     * declarations.
     * <p>
     * <strong>Note:</strong> The name should be a unique symbol. The
     * SymbolTable can be used for this purpose.
     *
     * @param name         The name of the entity.
     * @param publicId     The public identifier of the entity.
     * @param literalSystemId     The system identifier of the entity.
     * @param baseSystemId The base system identifier of the entity.
     *                     This is the system identifier of the entity
     *                     where <em>the entity being added</em> and
     *                     is used to expand the system identifier when
     *                     the system identifier is a relative URI.
     *                     When null the system identifier of the first
     *                     external entity on the stack is used instead.
     *
     * @see SymbolTable
     */
public void addExternalEntity(String name, String publicId, String literalSystemId, String baseSystemId) throws IOException {
    if (!fEntities.containsKey(name)) {
        if (baseSystemId == null) {
            // search for the first external entity on the stack  
            int size = fEntityStack.size();
            if (size == 0 && fCurrentEntity != null && fCurrentEntity.entityLocation != null) {
                baseSystemId = fCurrentEntity.entityLocation.getExpandedSystemId();
            }
            for (int i = size - 1; i >= 0; i--) {
                ScannedEntity externalEntity = (ScannedEntity) fEntityStack.elementAt(i);
                if (externalEntity.entityLocation != null && externalEntity.entityLocation.getExpandedSystemId() != null) {
                    baseSystemId = externalEntity.entityLocation.getExpandedSystemId();
                    break;
                }
            }
        }
        Entity entity = new ExternalEntity(name, new XMLEntityDescriptionImpl(name, publicId, literalSystemId, baseSystemId, expandSystemId(literalSystemId, baseSystemId, false)), null, fInExternalSubset);
        fEntities.put(name, entity);
    } else {
        if (fWarnDuplicateEntityDef) {
            fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "MSG_DUPLICATE_ENTITY_DEFINITION", new Object[] { name }, XMLErrorReporter.SEVERITY_WARNING);
        }
    }
}
