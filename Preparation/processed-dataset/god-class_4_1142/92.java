private Vector findDependentNamespaces(String namespace, Hashtable table) {
    final String ns = null2EmptyString(namespace);
    Vector namespaceList = (Vector) table.get(ns);
    if (namespaceList == null) {
        namespaceList = new Vector();
        table.put(ns, namespaceList);
    }
    return namespaceList;
}
