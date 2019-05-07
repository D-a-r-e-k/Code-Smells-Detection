// end constructTrees  
private boolean isExistingGrammar(XSDDescription desc, boolean ignoreConflict) {
    SchemaGrammar sg = fGrammarBucket.getGrammar(desc.getTargetNamespace());
    if (sg == null) {
        return findGrammar(desc, ignoreConflict) != null;
    } else if (sg.isImmutable()) {
        return true;
    } else {
        try {
            return sg.getDocumentLocations().contains(XMLEntityManager.expandSystemId(desc.getLiteralSystemId(), desc.getBaseSystemId(), false));
        } catch (MalformedURIException e) {
            return false;
        }
    }
}
