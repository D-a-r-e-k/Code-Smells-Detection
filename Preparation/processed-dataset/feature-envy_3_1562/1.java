static void performReferentialActions(Session session, Table table, RowSetNavigatorDataChange navigator, Row row, Object[] data, int[] changedCols, HashSet path) {
    if (!session.database.isReferentialIntegrity()) {
        return;
    }
    boolean delete = data == null;
    for (int i = 0, size = table.fkMainConstraints.length; i < size; i++) {
        Constraint c = table.fkMainConstraints[i];
        int action = delete ? c.core.deleteAction : c.core.updateAction;
        if (!delete) {
            if (!ArrayUtil.haveCommonElement(changedCols, c.core.mainCols)) {
                continue;
            }
            if (c.core.mainIndex.compareRowNonUnique(session, row.getData(), data, c.core.mainCols) == 0) {
                continue;
            }
        }
        RowIterator refiterator = c.findFkRef(session, row.getData());
        if (!refiterator.hasNext()) {
            refiterator.release();
            continue;
        }
        while (refiterator.hasNext()) {
            Row refRow = refiterator.getNextRow();
            Object[] refData = null;
            /** @todo use MATCH */
            if (c.core.refIndex.compareRowNonUnique(session, refRow.getData(), row.getData(), c.core.mainCols) != 0) {
                break;
            }
            if (delete && refRow.getId() == row.getId()) {
                continue;
            }
            switch(action) {
                case SchemaObject.ReferentialAction.CASCADE:
                    {
                        if (delete) {
                            if (navigator.addRow(refRow)) {
                                performReferentialActions(session, c.core.refTable, navigator, refRow, null, null, path);
                            }
                            continue;
                        }
                        refData = c.core.refTable.getEmptyRowData();
                        System.arraycopy(refRow.getData(), 0, refData, 0, refData.length);
                        for (int j = 0; j < c.core.refCols.length; j++) {
                            refData[c.core.refCols[j]] = data[c.core.mainCols[j]];
                        }
                        break;
                    }
                case SchemaObject.ReferentialAction.SET_NULL:
                    {
                        refData = c.core.refTable.getEmptyRowData();
                        System.arraycopy(refRow.getData(), 0, refData, 0, refData.length);
                        for (int j = 0; j < c.core.refCols.length; j++) {
                            refData[c.core.refCols[j]] = null;
                        }
                        break;
                    }
                case SchemaObject.ReferentialAction.SET_DEFAULT:
                    {
                        refData = c.core.refTable.getEmptyRowData();
                        System.arraycopy(refRow.getData(), 0, refData, 0, refData.length);
                        for (int j = 0; j < c.core.refCols.length; j++) {
                            ColumnSchema col = c.core.refTable.getColumn(c.core.refCols[j]);
                            refData[c.core.refCols[j]] = col.getDefaultValue(session);
                        }
                        break;
                    }
                case SchemaObject.ReferentialAction.NO_ACTION:
                case SchemaObject.ReferentialAction.RESTRICT:
                    {
                        if (navigator.containsDeletedRow(refRow)) {
                            continue;
                        }
                        int errorCode = c.core.deleteAction == SchemaObject.ReferentialAction.NO_ACTION ? ErrorCode.X_23504 : ErrorCode.X_23001;
                        String[] info = new String[] { c.core.refName.name, c.core.refTable.getName().name };
                        refiterator.release();
                        throw Error.error(null, errorCode, ErrorCode.CONSTRAINT, info);
                    }
                default:
                    continue;
            }
            refData = navigator.addRow(session, refRow, refData, table.getColumnTypes(), c.core.refCols);
            if (!path.add(c)) {
                continue;
            }
            performReferentialActions(session, c.core.refTable, navigator, refRow, refData, c.core.refCols, path);
            path.remove(c);
        }
        refiterator.release();
    }
}
