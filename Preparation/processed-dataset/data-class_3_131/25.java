protected Iterator getBindVariableIterator() {
    List list = new ArrayList();
    for (int i = 0; i < _select.size(); i++) {
        appendBindVariables(getSelect(i), list);
    }
    appendBindVariables(getWhere(), list);
    appendBindVariables(getLimit(), list);
    appendBindVariables(getOffset(), list);
    return list.iterator();
}
