/**
	 * saves an RPObject to the database
	 *
	 * @param object RPObject to save
	 * @return objectid
	 * @throws IOException in case of an input/output error
	 * @throws SQLException in case of an database error
	 */
public int storeRPObject(RPObject object) throws IOException, SQLException {
    DBTransaction transaction = TransactionPool.get().beginWork();
    try {
        int res = storeRPObject(transaction, object);
        return res;
    } finally {
        TransactionPool.get().commit(transaction);
    }
}
