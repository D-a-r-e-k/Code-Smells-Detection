/**
	 * does the rpobject with the specified database id exist?
	 *
	 * @param objectid database-id of the RPObject
	 * @return true, if it exists; false otherwise
	 * @throws SQLException in case of an database error
	 */
public boolean hasRPObject(int objectid) throws SQLException {
    DBTransaction transaction = TransactionPool.get().beginWork();
    try {
        boolean res = hasRPObject(transaction, objectid);
        return res;
    } finally {
        TransactionPool.get().commit(transaction);
    }
}
