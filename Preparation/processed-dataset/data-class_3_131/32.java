private void resolve(Database db) throws AxionException {
    if (!_resolved) {
        // resolve the Seletables 
        TableIdentifier[] tables = getFromArray();
        // resolve SELECT part 
        for (int i = 0; i < getSelectCount(); i++) {
            setSelect(i, db.resolveSelectable(getSelect(i), tables));
        }
        db.resolveFromNode(getFrom(), tables);
        // resolve WHERE part 
        db.resolveWhereNode(getWhere(), tables);
        // resolve ORDER BY part 
        if (null != _orderBy) {
            for (int i = 0; i < _orderBy.size(); i++) {
                OrderNode ob = (OrderNode) (_orderBy.get(i));
                ob.setSelectable(db.resolveSelectable(ob.getSelectable(), tables));
            }
        }
        _resolved = true;
        // check for aggregate functions 
        boolean foundScalar = false;
        for (int i = 0; i < getSelectCount(); i++) {
            if (getSelect(i) instanceof AggregateFunction) {
                if (foundScalar) {
                    throw new AxionException("Can't select both scalar values and aggregate functions.");
                } else if (_foundAggregateFunction) {
                    throw new AxionException("Currently can't select more than one aggregate function at a time.");
                } else {
                    _foundAggregateFunction = true;
                }
            } else {
                if (_foundAggregateFunction) {
                    throw new AxionException("Can't select both scalar values and aggregate functions.");
                }
                foundScalar = true;
            }
        }
    }
}
