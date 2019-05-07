// getSchemaDocument1(boolean, boolean, XMLInputSource, Element): Element  
/**
     * getSchemaDocument method uses XMLInputSource to parse a schema document.
     * @param schemaNamespace
     * @param schemaSource
     * @param mustResolve
     * @param referType
     * @param referElement
     * @return A schema Element.
     */
private Element getSchemaDocument(XSInputSource schemaSource, XSDDescription desc) {
    SchemaGrammar[] grammars = schemaSource.getGrammars();
    short referType = desc.getContextType();
    if (grammars != null && grammars.length > 0) {
        Vector expandedGrammars = expandGrammars(grammars);
        // check for existing grammars in our bucket  
        // and if there exist any, and namespace growth is  
        // not enabled - we do nothing  
        if (fNamespaceGrowth || !existingGrammars(expandedGrammars)) {
            addGrammars(expandedGrammars);
            if (referType == XSDDescription.CONTEXT_PREPARSE) {
                desc.setTargetNamespace(grammars[0].getTargetNamespace());
            }
        }
    } else {
        XSObject[] components = schemaSource.getComponents();
        if (components != null && components.length > 0) {
            Hashtable importDependencies = new Hashtable();
            Vector expandedComponents = expandComponents(components, importDependencies);
            if (fNamespaceGrowth || canAddComponents(expandedComponents)) {
                addGlobalComponents(expandedComponents, importDependencies);
                if (referType == XSDDescription.CONTEXT_PREPARSE) {
                    desc.setTargetNamespace(components[0].getNamespace());
                }
            }
        }
    }
    return null;
}
