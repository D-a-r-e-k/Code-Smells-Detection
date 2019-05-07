private void setLocationHints(XSDDescription desc, String[] locations, SchemaGrammar grammar) {
    int length = locations.length;
    if (grammar == null) {
        fXSDDescription.fLocationHints = new String[length];
        System.arraycopy(locations, 0, fXSDDescription.fLocationHints, 0, length);
    } else {
        setLocationHints(desc, locations, grammar.getDocumentLocations());
    }
}
