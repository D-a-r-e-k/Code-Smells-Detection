private void addGlobalComponents(Vector components, Hashtable importDependencies) {
    final XSDDescription desc = new XSDDescription();
    final int size = components.size();
    for (int i = 0; i < size; i++) {
        addGlobalComponent((XSObject) components.elementAt(i), desc);
    }
    updateImportDependencies(importDependencies);
}
