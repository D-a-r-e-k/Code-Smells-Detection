private Boolean getAllAnyValue(Session session, Object[] data, SubQuery subquery) {
    Table table = subquery.getTable();
    boolean empty = table.isEmpty(session);
    Index index = table.getFullIndex();
    RowIterator it;
    Row firstrow;
    PersistentStore store = session.sessionData.getRowStore(table);
    Row lastrow = index.lastRow(session, store).getNextRow();
    Object[] lastdata;
    Object[] firstdata;
    switch(exprSubType) {
        case OpTypes.ANY_QUANTIFIED:
            {
                if (empty) {
                    return Boolean.FALSE;
                }
                if (countNulls(data) == data.length) {
                    return null;
                }
                lastdata = lastrow.getData();
                if (countNulls(lastdata) == data.length) {
                    return null;
                }
                convertToType(session, data, nodes[LEFT].nodeDataTypes, nodes[RIGHT].nodeDataTypes);
                if (opType == OpTypes.EQUAL) {
                    it = index.findFirstRow(session, store, data);
                    return it.hasNext() ? Boolean.TRUE : Boolean.FALSE;
                }
                it = index.findFirstRowNotNull(session, store);
                firstrow = it.getNextRow();
                firstdata = firstrow.getData();
                Boolean comparefirst = compareValues(session, data, firstdata);
                Boolean comparelast = compareValues(session, data, lastdata);
                switch(opType) {
                    case OpTypes.NOT_EQUAL:
                        return Boolean.TRUE.equals(comparefirst) || Boolean.TRUE.equals(comparelast) ? Boolean.TRUE : Boolean.FALSE;
                    case OpTypes.GREATER:
                        return comparefirst;
                    case OpTypes.GREATER_EQUAL:
                        return comparefirst;
                    case OpTypes.SMALLER:
                        return comparelast;
                    case OpTypes.SMALLER_EQUAL:
                        return comparelast;
                }
                break;
            }
        case OpTypes.ALL_QUANTIFIED:
            {
                if (empty) {
                    return Boolean.TRUE;
                }
                if (countNulls(data) == data.length) {
                    return null;
                }
                it = index.firstRow(session, store);
                firstrow = it.getNextRow();
                firstdata = firstrow.getData();
                if (countNulls(firstdata) == data.length) {
                    return null;
                }
                convertToType(session, data, nodes[LEFT].nodeDataTypes, nodes[RIGHT].nodeDataTypes);
                it = index.findFirstRow(session, store, data);
                if (opType == OpTypes.EQUAL) {
                    if (it.hasNext()) {
                        return subquery.getTable().getRowCount(store) == 1 ? Boolean.TRUE : Boolean.FALSE;
                    } else {
                        return Boolean.FALSE;
                    }
                }
                if (opType == OpTypes.NOT_EQUAL) {
                    return it.hasNext() ? Boolean.FALSE : Boolean.TRUE;
                }
                lastdata = lastrow.getData();
                Boolean comparefirst = compareValues(session, data, firstdata);
                Boolean comparelast = compareValues(session, data, lastdata);
                switch(opType) {
                    case OpTypes.GREATER:
                        return comparelast;
                    case OpTypes.GREATER_EQUAL:
                        return comparelast;
                    case OpTypes.SMALLER:
                        return comparefirst;
                    case OpTypes.SMALLER_EQUAL:
                        return comparefirst;
                }
                break;
            }
    }
    return null;
}
