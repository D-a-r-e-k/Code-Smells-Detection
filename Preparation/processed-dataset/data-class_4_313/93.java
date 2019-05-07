private void addNamespaceDependency(String namespace1, String namespace2, Vector list) {
    final String ns1 = null2EmptyString(namespace1);
    final String ns2 = null2EmptyString(namespace2);
    if (!ns1.equals(ns2)) {
        if (!list.contains(ns2)) {
            list.add(ns2);
        }
    }
}
