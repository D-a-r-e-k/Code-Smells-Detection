public Object getValue(Session session) {
    switch(opType) {
        case OpTypes.VALUE:
            return valueData;
        case OpTypes.SIMPLE_COLUMN:
            {
                Object[] data = session.sessionContext.rangeIterators[rangePosition].getCurrent();
                return data[columnIndex];
            }
        case OpTypes.ROW:
            {
                if (nodes.length == 1) {
                    return nodes[0].getValue(session);
                }
                Object[] row = new Object[nodes.length];
                for (int i = 0; i < nodes.length; i++) {
                    row[i] = nodes[i].getValue(session);
                }
                return row;
            }
        case OpTypes.ARRAY:
            {
                Object[] array = new Object[nodes.length];
                for (int i = 0; i < nodes.length; i++) {
                    array[i] = nodes[i].getValue(session);
                }
                return array;
            }
        case OpTypes.ARRAY_SUBQUERY:
            {
                subQuery.materialiseCorrelated(session);
                RowSetNavigatorData nav = subQuery.getNavigator(session);
                int size = nav.getSize();
                Object[] array = new Object[size];
                nav.beforeFirst();
                for (int i = 0; nav.hasNext(); i++) {
                    Object[] data = nav.getNextRowData();
                    array[i] = data[0];
                }
                return array;
            }
        case OpTypes.TABLE_SUBQUERY:
        case OpTypes.ROW_SUBQUERY:
            {
                subQuery.materialiseCorrelated(session);
                Object[] value = subQuery.getValues(session);
                if (value.length == 1) {
                    return ((Object[]) value)[0];
                }
                return value;
            }
        default:
            throw Error.runtimeError(ErrorCode.U_S0500, "Expression");
    }
}
