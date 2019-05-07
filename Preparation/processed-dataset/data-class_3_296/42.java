private void setReferences() {
    OrderedHashSet set = new OrderedHashSet();
    for (int i = 0; i < parameterTypes.length; i++) {
        ColumnSchema param = (ColumnSchema) parameterList.get(i);
        set.addAll(param.getReferences());
    }
    if (statement != null) {
        set.addAll(statement.getReferences());
    }
    references = set;
}
