public Result getResult(Session session) {
    switch(opType) {
        case OpTypes.ARRAY:
            {
                RowSetNavigatorData navigator = subQuery.getNavigator(session);
                Object[] array = new Object[navigator.getSize()];
                navigator.beforeFirst();
                for (int i = 0; navigator.hasNext(); i++) {
                    Object[] data = navigator.getNext();
                    array[i] = data[0];
                }
                return Result.newPSMResult(array);
            }
        case OpTypes.TABLE_SUBQUERY:
            {
                subQuery.materialiseCorrelated(session);
                RowSetNavigatorData navigator = subQuery.getNavigator(session);
                Result result = Result.newResult(navigator);
                result.metaData = subQuery.queryExpression.getMetaData();
                return result;
            }
        default:
            {
                Object value = getValue(session);
                return Result.newPSMResult(value);
            }
    }
}
