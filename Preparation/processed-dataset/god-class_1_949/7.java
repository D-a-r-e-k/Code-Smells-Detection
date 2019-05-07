/**
	 * loads an RPObject form the database, using the factory to create the correct subclass
	 *
	 * @param objectid database-id of the RPObject
	 * @param transform use the factory to create a subclass of RPObject
	 * @return RPObject
	 * @throws IOException in case of an input/output error
	 * @throws SQLException in case of an database error
	 */
public RPObject loadRPObject(int objectid, boolean transform) throws SQLException, IOException {
    DBTransaction transaction = TransactionPool.get().beginWork();
    try {
        RPObject res = loadRPObject(transaction, objectid, transform);
        return res;
    } finally {
        TransactionPool.get().commit(transaction);
    }
}
