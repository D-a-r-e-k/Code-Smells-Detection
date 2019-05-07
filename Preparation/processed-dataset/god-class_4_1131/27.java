// reset(XMLComponentManager)  
// reset general state.  Should not be called other than by  
// a class acting as a component manager but not  
// implementing that interface for whatever reason.  
public void reset() {
    fEntityExpansionLimit = (fSecurityManager != null) ? fSecurityManager.getEntityExpansionLimit() : 0;
    // initialize state  
    fStandalone = false;
    fHasPEReferences = false;
    fEntities.clear();
    fEntityStack.removeAllElements();
    fEntityExpansionCount = 0;
    fCurrentEntity = null;
    // reset scanner  
    if (fXML10EntityScanner != null) {
        fXML10EntityScanner.reset(fSymbolTable, this, fErrorReporter);
    }
    if (fXML11EntityScanner != null) {
        fXML11EntityScanner.reset(fSymbolTable, this, fErrorReporter);
    }
    // DEBUG  
    if (DEBUG_ENTITIES) {
        addInternalEntity("text", "Hello, World.");
        addInternalEntity("empty-element", "<foo/>");
        addInternalEntity("balanced-element", "<foo></foo>");
        addInternalEntity("balanced-element-with-text", "<foo>Hello, World</foo>");
        addInternalEntity("balanced-element-with-entity", "<foo>&text;</foo>");
        addInternalEntity("unbalanced-entity", "<foo>");
        addInternalEntity("recursive-entity", "<foo>&recursive-entity2;</foo>");
        addInternalEntity("recursive-entity2", "<bar>&recursive-entity3;</bar>");
        addInternalEntity("recursive-entity3", "<baz>&recursive-entity;</baz>");
        try {
            addExternalEntity("external-text", null, "external-text.ent", "test/external-text.xml");
            addExternalEntity("external-balanced-element", null, "external-balanced-element.ent", "test/external-balanced-element.xml");
            addExternalEntity("one", null, "ent/one.ent", "test/external-entity.xml");
            addExternalEntity("two", null, "ent/two.ent", "test/ent/one.xml");
        } catch (IOException ex) {
        }
    }
    // copy declared entities  
    if (fDeclaredEntities != null) {
        Iterator entries = fDeclaredEntities.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            fEntities.put(key, value);
        }
    }
    fEntityHandler = null;
}
