// resolveEntity(XMLResourceIdentifier):XMLInputSource  
/**
     * Starts a named entity.
     *
     * @param entityName The name of the entity to start.
     * @param literal    True if this entity is started within a literal
     *                   value.
     *
     * @throws IOException  Thrown on i/o error.
     * @throws XNIException Thrown by entity handler to signal an error.
     */
public void startEntity(String entityName, boolean literal) throws IOException, XNIException {
    // was entity declared?  
    Entity entity = (Entity) fEntities.get(entityName);
    if (entity == null) {
        if (fEntityHandler != null) {
            String encoding = null;
            fResourceIdentifier.clear();
            fEntityAugs.removeAllItems();
            fEntityAugs.putItem(Constants.ENTITY_SKIPPED, Boolean.TRUE);
            fEntityHandler.startEntity(entityName, fResourceIdentifier, encoding, fEntityAugs);
            fEntityAugs.removeAllItems();
            fEntityAugs.putItem(Constants.ENTITY_SKIPPED, Boolean.TRUE);
            fEntityHandler.endEntity(entityName, fEntityAugs);
        }
        return;
    }
    // should we skip external entities?  
    boolean external = entity.isExternal();
    if (external && (fValidationManager == null || !fValidationManager.isCachedDTD())) {
        boolean unparsed = entity.isUnparsed();
        boolean parameter = entityName.startsWith("%");
        boolean general = !parameter;
        if (unparsed || (general && !fExternalGeneralEntities) || (parameter && !fExternalParameterEntities)) {
            if (fEntityHandler != null) {
                fResourceIdentifier.clear();
                final String encoding = null;
                ExternalEntity externalEntity = (ExternalEntity) entity;
                //REVISIT:  since we're storing expandedSystemId in the  
                // externalEntity, how could this have got here if it wasn't already  
                // expanded??? - neilg  
                String extLitSysId = (externalEntity.entityLocation != null ? externalEntity.entityLocation.getLiteralSystemId() : null);
                String extBaseSysId = (externalEntity.entityLocation != null ? externalEntity.entityLocation.getBaseSystemId() : null);
                String expandedSystemId = expandSystemId(extLitSysId, extBaseSysId, false);
                fResourceIdentifier.setValues((externalEntity.entityLocation != null ? externalEntity.entityLocation.getPublicId() : null), extLitSysId, extBaseSysId, expandedSystemId);
                fEntityAugs.removeAllItems();
                fEntityAugs.putItem(Constants.ENTITY_SKIPPED, Boolean.TRUE);
                fEntityHandler.startEntity(entityName, fResourceIdentifier, encoding, fEntityAugs);
                fEntityAugs.removeAllItems();
                fEntityAugs.putItem(Constants.ENTITY_SKIPPED, Boolean.TRUE);
                fEntityHandler.endEntity(entityName, fEntityAugs);
            }
            return;
        }
    }
    // is entity recursive?  
    int size = fEntityStack.size();
    for (int i = size; i >= 0; i--) {
        Entity activeEntity = i == size ? fCurrentEntity : (Entity) fEntityStack.elementAt(i);
        if (activeEntity.name == entityName) {
            StringBuffer path = new StringBuffer(entityName);
            for (int j = i + 1; j < size; j++) {
                activeEntity = (Entity) fEntityStack.elementAt(j);
                path.append(" -> ");
                path.append(activeEntity.name);
            }
            path.append(" -> ");
            path.append(fCurrentEntity.name);
            path.append(" -> ");
            path.append(entityName);
            fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "RecursiveReference", new Object[] { entityName, path.toString() }, XMLErrorReporter.SEVERITY_FATAL_ERROR);
            if (fEntityHandler != null) {
                fResourceIdentifier.clear();
                final String encoding = null;
                if (external) {
                    ExternalEntity externalEntity = (ExternalEntity) entity;
                    // REVISIT:  for the same reason above...  
                    String extLitSysId = (externalEntity.entityLocation != null ? externalEntity.entityLocation.getLiteralSystemId() : null);
                    String extBaseSysId = (externalEntity.entityLocation != null ? externalEntity.entityLocation.getBaseSystemId() : null);
                    String expandedSystemId = expandSystemId(extLitSysId, extBaseSysId, false);
                    fResourceIdentifier.setValues((externalEntity.entityLocation != null ? externalEntity.entityLocation.getPublicId() : null), extLitSysId, extBaseSysId, expandedSystemId);
                }
                fEntityAugs.removeAllItems();
                fEntityAugs.putItem(Constants.ENTITY_SKIPPED, Boolean.TRUE);
                fEntityHandler.startEntity(entityName, fResourceIdentifier, encoding, fEntityAugs);
                fEntityAugs.removeAllItems();
                fEntityAugs.putItem(Constants.ENTITY_SKIPPED, Boolean.TRUE);
                fEntityHandler.endEntity(entityName, fEntityAugs);
            }
            return;
        }
    }
    // resolve external entity  
    XMLInputSource xmlInputSource = null;
    if (external) {
        ExternalEntity externalEntity = (ExternalEntity) entity;
        xmlInputSource = resolveEntity(externalEntity.entityLocation);
    } else {
        InternalEntity internalEntity = (InternalEntity) entity;
        Reader reader = new StringReader(internalEntity.text);
        xmlInputSource = new XMLInputSource(null, null, null, reader, null);
    }
    // start the entity  
    startEntity(entityName, xmlInputSource, literal, external);
}
