private void updateImportDependencies(Hashtable table) {
    Enumeration keys = table.keys();
    String namespace;
    Vector importList;
    while (keys.hasMoreElements()) {
        namespace = (String) keys.nextElement();
        importList = (Vector) table.get(null2EmptyString(namespace));
        if (importList.size() > 0) {
            expandImportList(namespace, importList);
        }
    }
}
