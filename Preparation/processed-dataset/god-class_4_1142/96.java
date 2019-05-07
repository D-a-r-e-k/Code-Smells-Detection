// createTraversers()  
// before parsing a schema, need to clear registries associated with  
// parsing schemas  
void prepareForParse() {
    fTraversed.clear();
    fDoc2SystemId.clear();
    fHiddenNodes.clear();
    fLastSchemaWasDuplicate = false;
}
