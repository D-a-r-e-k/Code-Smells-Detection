// initialize all the traversers.  
// this should only need to be called once during the construction  
// of this object; it creates the traversers that will be used to  
// construct schemaGrammars.  
private void createTraversers() {
    fAttributeChecker = new XSAttributeChecker(this);
    fAttributeGroupTraverser = new XSDAttributeGroupTraverser(this, fAttributeChecker);
    fAttributeTraverser = new XSDAttributeTraverser(this, fAttributeChecker);
    fComplexTypeTraverser = new XSDComplexTypeTraverser(this, fAttributeChecker);
    fElementTraverser = new XSDElementTraverser(this, fAttributeChecker);
    fGroupTraverser = new XSDGroupTraverser(this, fAttributeChecker);
    fKeyrefTraverser = new XSDKeyrefTraverser(this, fAttributeChecker);
    fNotationTraverser = new XSDNotationTraverser(this, fAttributeChecker);
    fSimpleTypeTraverser = new XSDSimpleTypeTraverser(this, fAttributeChecker);
    fUniqueOrKeyTraverser = new XSDUniqueOrKeyTraverser(this, fAttributeChecker);
    fWildCardTraverser = new XSDWildcardTraverser(this, fAttributeChecker);
}
