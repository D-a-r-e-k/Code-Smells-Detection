/**
     * Finds a match with a row from a different table
     *
     * @param session Session
     * @param store PersistentStore
     * @param rowdata array containing data for the index columns
     * @param rowColMap map of the data to columns
     * @param fieldCount int
     * @param compareType int
     * @param readMode int
     * @return matching node or null
     */
NodeAVL findNode(Session session, PersistentStore store, Object[] rowdata, int[] rowColMap, int fieldCount, int compareType, int readMode, boolean reversed) {
    readLock.lock();
    try {
        NodeAVL x = getAccessor(store);
        NodeAVL n = null;
        NodeAVL result = null;
        Row currentRow = null;
        if (compareType != OpTypes.EQUAL && compareType != OpTypes.IS_NULL) {
            fieldCount--;
        }
        while (x != null) {
            currentRow = x.getRow(store);
            int i = 0;
            if (fieldCount > 0) {
                i = compareRowNonUnique(session, currentRow.getData(), rowdata, rowColMap, fieldCount);
            }
            if (i == 0) {
                switch(compareType) {
                    case OpTypes.IS_NULL:
                    case OpTypes.EQUAL:
                        {
                            result = x;
                            n = x.getLeft(store);
                            break;
                        }
                    case OpTypes.NOT:
                    case OpTypes.GREATER:
                        {
                            i = compareObject(session, currentRow.getData(), rowdata, rowColMap, fieldCount);
                            if (i <= 0) {
                                n = x.getRight(store);
                            } else {
                                result = x;
                                n = x.getLeft(store);
                            }
                            break;
                        }
                    case OpTypes.GREATER_EQUAL:
                        {
                            i = compareObject(session, currentRow.getData(), rowdata, rowColMap, fieldCount);
                            if (i < 0) {
                                n = x.getRight(store);
                            } else {
                                result = x;
                                n = x.getLeft(store);
                            }
                            break;
                        }
                    case OpTypes.SMALLER:
                        {
                            i = compareObject(session, currentRow.getData(), rowdata, rowColMap, fieldCount);
                            if (i < 0) {
                                result = x;
                                n = x.getRight(store);
                            } else {
                                n = x.getLeft(store);
                            }
                            break;
                        }
                    case OpTypes.SMALLER_EQUAL:
                        {
                            i = compareObject(session, currentRow.getData(), rowdata, rowColMap, fieldCount);
                            if (i <= 0) {
                                result = x;
                                n = x.getRight(store);
                            } else {
                                n = x.getLeft(store);
                            }
                            break;
                        }
                    default:
                        Error.runtimeError(ErrorCode.U_S0500, "Index");
                }
            } else if (i < 0) {
                n = x.getRight(store);
            } else if (i > 0) {
                n = x.getLeft(store);
            }
            if (n == null) {
                break;
            }
            x = n;
        }
        // MVCC 190 
        if (session == null) {
            return result;
        }
        while (result != null) {
            currentRow = result.getRow(store);
            if (session.database.txManager.canRead(session, currentRow, readMode, colIndex)) {
                break;
            }
            result = reversed ? last(store, result) : next(store, result);
            if (result == null) {
                break;
            }
            currentRow = result.getRow(store);
            if (fieldCount > 0 && compareRowNonUnique(session, currentRow.getData(), rowdata, rowColMap, fieldCount) != 0) {
                result = null;
                break;
            }
        }
        return result;
    } finally {
        readLock.unlock();
    }
}
