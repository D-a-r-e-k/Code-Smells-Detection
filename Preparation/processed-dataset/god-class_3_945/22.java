private Datasource getDatasource() {
    List services = JagGenerator.getObjectsFromTree(Datasource.class);
    for (int i = 0; i < services.size(); i++) {
        Datasource d = (Datasource) services.get(i);
        return d;
    }
    return null;
}
