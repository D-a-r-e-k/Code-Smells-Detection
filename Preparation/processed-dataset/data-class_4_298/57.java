/**
     * Finds a match with a value
     *
     * @param session Session
     * @param store PersistentStore
     * @param data value data for the index columns
     * @param compareType int
     * @param readMode int
     * @return matching node or null
     */
NodeAVL findNode(Session session, PersistentStore store, Object data, int compareType, int readMode) {
    readLock.lock();
    try {
        NodeAVL x = getAccessor(store);
        NodeAVL n = null;
        NodeAVL result = null;
        Row currentRow = null;
        while (x != null) {
            currentRow = x.getRow(store);
            int i = colTypes[0].compare(session, data, currentRow.getData()[colIndex[0]]);
            switch(compareType) {
                case OpTypes.IS_NULL:
                case OpTypes.EQUAL:
                    {
                        if (i == 0) {
                            result = x;
                            n = x.getLeft(store);
                            break;
                        } else if (i > 0) {
                            n = x.getRight(store);
                        } else if (i < 0) {
                            n = x.getLeft(store);
                        }
                        break;
                    }
                case OpTypes.NOT:
                case OpTypes.GREATER:
                    {
                        if (i >= 0) {
                            n = x.getRight(store);
                        } else {
                            result = x;
                            n = x.getLeft(store);
                        }
                        break;
                    }
                case OpTypes.GREATER_EQUAL:
                    {
                        if (i > 0) {
                            n = x.getRight(store);
                        } else {
                            result = x;
                            n = x.getLeft(store);
                        }
                        break;
                    }
                default:
                    Error.runtimeError(ErrorCode.U_S0500, "Index");
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
            result = next(store, result);
            if (compareType == OpTypes.EQUAL) {
                if (colTypes[0].compare(session, data, currentRow.getData()[colIndex[0]]) != 0) {
                    result = null;
                    break;
                }
            }
        }
        return result;
    } finally {
        readLock.unlock();
    }
}
