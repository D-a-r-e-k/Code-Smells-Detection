private void processQuery(Database db) throws AxionException {
    _currentDatabase = db;
    // the map of column identifiers to field locations (index of column in row's array) 
    _colIdToFieldMap = new HashMap();
    _indexOffset = 0;
    _literals = createLiteralList();
    RowIterator rows = null;
    _unappliedWhereNodes = processWhereTree(getWhere());
    rows = processFromTree(_from, db);
    // And apply any remaining where nodes to the join. 
    Iterator unappliedWhereNodeIter = _unappliedWhereNodes.iterator();
    while (unappliedWhereNodeIter.hasNext()) {
        WhereNode node = (WhereNode) (unappliedWhereNodeIter.next());
        rows = new FilteringRowIterator(rows, new RowDecorator(_colIdToFieldMap), node);
    }
    _selected = generateSelectArrayForResultSet(db);
    // apply distinct, if needed 
    if (_distinct) {
        rows = new DistinctRowIterator(rows, _colIdToFieldMap, _selected);
    }
    // apply the ORDER BY if any 
    if (getOrderByCount() > 0) {
        ComparatorChain orderChain = generateOrderChain(_colIdToFieldMap);
        ArrayList list = new ArrayList();
        while (rows.hasNext()) {
            list.add(rows.next());
        }
        Collections.sort(list, orderChain);
        rows = new ListIteratorRowIterator(list.listIterator());
    }
    // if there's a limit, apply it 
    if (null != getLimit() || null != getOffset()) {
        rows = new LimitingRowIterator(rows, getLimit(), getOffset());
    }
    // We're done. 
    _rows = rows;
}
