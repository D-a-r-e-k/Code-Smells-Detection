private Result buildResult(Session session, int limitcount) {
    RowSetNavigatorData navigator = new RowSetNavigatorData(session, (QuerySpecification) this);
    Result result = Result.newResult(navigator);
    result.metaData = resultMetaData;
    if (isUpdatable) {
        result.rsProperties = ResultProperties.updatablePropsValue;
    }
    if (this.isSimpleCount) {
        Object[] data = new Object[indexLimitData];
        Table table = rangeVariables[0].getTable();
        PersistentStore store = table.getRowStore(session);
        int count = table.getIndex(0).size(session, store);
        data[0] = data[indexStartAggregates] = ValuePool.getInt(count);
        navigator.add(data);
        return result;
    }
    int fullJoinIndex = 0;
    RangeIterator[] rangeIterators = new RangeIterator[rangeVariables.length];
    for (int i = 0; i < rangeVariables.length; i++) {
        rangeIterators[i] = rangeVariables[i].getIterator(session);
    }
    for (int currentIndex = 0; ; ) {
        if (currentIndex < fullJoinIndex) {
            // finished current span 
            // or finished outer rows on right navigator 
            boolean end = true;
            for (int i = fullJoinIndex + 1; i < rangeVariables.length; i++) {
                if (rangeVariables[i].isRightJoin) {
                    fullJoinIndex = i;
                    currentIndex = i;
                    end = false;
                    ((RangeIteratorRight) rangeIterators[i]).setOnOuterRows();
                    break;
                }
            }
            if (end) {
                break;
            }
        }
        RangeIterator it = rangeIterators[currentIndex];
        if (it.next()) {
            if (currentIndex < rangeVariables.length - 1) {
                currentIndex++;
                continue;
            }
        } else {
            it.reset();
            currentIndex--;
            continue;
        }
        session.sessionData.startRowProcessing();
        Object[] data = new Object[indexLimitData];
        for (int i = 0; i < indexStartAggregates; i++) {
            if (isAggregated && aggregateCheck[i]) {
                continue;
            } else {
                data[i] = exprColumns[i].getValue(session);
            }
        }
        for (int i = indexLimitVisible; i < indexLimitRowId; i++) {
            if (i == indexLimitVisible) {
                data[i] = it.getRowidObject();
            } else {
                data[i] = it.getCurrentRow();
            }
        }
        Object[] groupData = null;
        if (isAggregated || isGrouped) {
            groupData = navigator.getGroupData(data);
            if (groupData != null) {
                data = groupData;
            }
        }
        for (int i = indexStartAggregates; i < indexLimitExpressions; i++) {
            data[i] = exprColumns[i].updateAggregatingValue(session, data[i]);
        }
        if (groupData == null) {
            navigator.add(data);
        } else if (isAggregated) {
            navigator.update(groupData, data);
        }
        int rowCount = navigator.getSize();
        if (rowCount == session.resultMaxMemoryRows && !isAggregated && !hasMemoryRow) {
            navigator = new RowSetNavigatorDataTable(session, this, navigator);
            result.setNavigator(navigator);
        }
        if (isAggregated || isGrouped) {
            if (!sortAndSlice.isGenerated) {
                continue;
            }
        }
        if (rowCount >= limitcount) {
            break;
        }
    }
    navigator.reset();
    for (int i = 0; i < rangeVariables.length; i++) {
        rangeIterators[i].reset();
    }
    if (!isGrouped && !isAggregated) {
        return result;
    }
    if (isAggregated) {
        if (!isGrouped && navigator.getSize() == 0) {
            Object[] data = new Object[exprColumns.length];
            navigator.add(data);
        }
        navigator.reset();
        session.sessionContext.setRangeIterator(navigator);
        while (navigator.next()) {
            Object[] data = navigator.getCurrent();
            for (int i = indexStartAggregates; i < indexLimitExpressions; i++) {
                data[i] = exprColumns[i].getAggregatedValue(session, data[i]);
            }
            for (int i = 0; i < indexStartAggregates; i++) {
                if (aggregateCheck[i]) {
                    data[i] = exprColumns[i].getValue(session);
                }
            }
        }
    }
    navigator.reset();
    if (havingCondition != null) {
        while (navigator.hasNext()) {
            Object[] data = (Object[]) navigator.getNext();
            if (!Boolean.TRUE.equals(data[indexLimitVisible + groupByColumnCount])) {
                navigator.remove();
            }
        }
        navigator.reset();
    }
    return result;
}
