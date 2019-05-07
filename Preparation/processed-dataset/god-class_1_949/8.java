/**
	 * loads an RPObject form the database
	 *
	 * @param objectid database-id of the RPObject
	 * @return RPObject
	 * @throws IOException in case of an input/output error
	 * @throws SQLException in case of an database error
	 */
public RPObject loadRPObject(int objectid) throws SQLException, IOException {
    DBTransaction transaction = TransactionPool.get().beginWork();
    try {
        RPObject res = loadRPObject(transaction, objectid, true);
        return res;
    } finally {
        TransactionPool.get().commit(transaction);
    }
}
