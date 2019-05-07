private Boolean testMatchCondition(Session session, Object[] data) {
    int nulls;
    if (data == null) {
        return Boolean.TRUE;
    }
    nulls = countNulls(data);
    if (nulls != 0) {
        switch(opType) {
            case OpTypes.MATCH_SIMPLE:
            case OpTypes.MATCH_UNIQUE_SIMPLE:
                return Boolean.TRUE;
            case OpTypes.MATCH_PARTIAL:
            case OpTypes.MATCH_UNIQUE_PARTIAL:
                if (nulls == data.length) {
                    return Boolean.TRUE;
                }
                break;
            case OpTypes.MATCH_FULL:
            case OpTypes.MATCH_UNIQUE_FULL:
                return nulls == data.length ? Boolean.TRUE : Boolean.FALSE;
        }
    }
    if (nodes[RIGHT].opType == OpTypes.TABLE) {
        final int length = nodes[RIGHT].nodes.length;
        boolean hasMatch = false;
        for (int i = 0; i < length; i++) {
            Object[] rowData = nodes[RIGHT].nodes[i].getRowValue(session);
            Boolean result = compareValues(session, data, rowData);
            if (result == null || !result.booleanValue()) {
                continue;
            }
            switch(opType) {
                case OpTypes.MATCH_SIMPLE:
                case OpTypes.MATCH_PARTIAL:
                case OpTypes.MATCH_FULL:
                    return Boolean.TRUE;
                case OpTypes.MATCH_UNIQUE_SIMPLE:
                case OpTypes.MATCH_UNIQUE_PARTIAL:
                case OpTypes.MATCH_UNIQUE_FULL:
                    if (hasMatch) {
                        return Boolean.FALSE;
                    }
                    hasMatch = true;
            }
        }
        return hasMatch ? Boolean.TRUE : Boolean.FALSE;
    } else if (nodes[RIGHT].opType == OpTypes.TABLE_SUBQUERY) {
        PersistentStore store = session.sessionData.getRowStore(nodes[RIGHT].getTable());
        nodes[RIGHT].materialise(session);
        convertToType(session, data, nodes[LEFT].nodeDataTypes, nodes[RIGHT].nodeDataTypes);
        if (nulls != 0 && (opType == OpTypes.MATCH_PARTIAL || opType == OpTypes.MATCH_UNIQUE_PARTIAL)) {
            boolean hasMatch = false;
            RowIterator it = nodes[RIGHT].getTable().rowIterator(session);
            while (it.hasNext()) {
                Object[] rowData = it.getNextRow().getData();
                Boolean result = compareValues(session, data, rowData);
                if (result == null) {
                    continue;
                }
                if (result.booleanValue()) {
                    if (opType == OpTypes.MATCH_PARTIAL) {
                        return Boolean.TRUE;
                    }
                    if (hasMatch) {
                        return Boolean.FALSE;
                    }
                    hasMatch = true;
                }
            }
            return hasMatch ? Boolean.TRUE : Boolean.FALSE;
        }
        RowIterator it = nodes[RIGHT].getTable().getPrimaryIndex().findFirstRow(session, store, data);
        boolean result = it.hasNext();
        if (!result) {
            return Boolean.FALSE;
        }
        switch(opType) {
            case OpTypes.MATCH_SIMPLE:
            case OpTypes.MATCH_PARTIAL:
            case OpTypes.MATCH_FULL:
                return Boolean.TRUE;
        }
        it.getNextRow();
        result = it.hasNext();
        if (!result) {
            return Boolean.TRUE;
        }
        Object[] rowData = it.getNextRow().getData();
        Boolean returnValue = Boolean.TRUE.equals(compareValues(session, data, rowData)) ? Boolean.FALSE : Boolean.TRUE;
        return returnValue;
    }
    throw Error.error(ErrorCode.X_42564);
}
