protected Collection getPorts(Object cell) {
    LinkedList list = new LinkedList();
    for (int i = 0; i < graphModel.getChildCount(cell); i++) {
        Object child = graphModel.getChild(cell, i);
        if (graphModel.isPort(child))
            list.add(child);
    }
    return list;
}
