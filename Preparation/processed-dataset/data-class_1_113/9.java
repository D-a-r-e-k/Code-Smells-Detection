/**
	 * deletes an RPObject from the database
	 *
	 * @param objectid database-id of the RPObject
	 * @return objectid
	 * @throws SQLException in case of an database error
	 */
public int removeRPObject(int objectid) throws SQLException {
    DBTransaction transaction = TransactionPool.get().beginWork();
    try {
        int res = removeRPObject(transaction, objectid);
        return res;
    } finally {
        TransactionPool.get().commit(transaction);
    }
}
