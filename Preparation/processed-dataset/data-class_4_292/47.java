Type[] getNodeDataTypes() {
    if (nodeDataTypes == null) {
        return new Type[] { dataType };
    } else {
        return nodeDataTypes;
    }
}
