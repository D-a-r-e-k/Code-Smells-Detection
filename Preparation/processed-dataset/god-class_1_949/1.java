/**
	 * loads an RPObject form the database, using the factory to create the correct subclass
	 *
	 * @param transaction DBTransaction
	 * @param objectid database-id of the RPObject
	 * @return RPObject
	 * @throws IOException in case of an input/output error
	 * @throws SQLException in case of an database error
	 */
public RPObject loadRPObject(DBTransaction transaction, int objectid) throws SQLException, IOException {
    return loadRPObject(transaction, objectid, true);
}
