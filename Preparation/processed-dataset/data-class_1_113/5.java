/**
	 * does the rpobject with the specified database id exist?
	 *
	 * @param transaction DBTransaction
	 * @param objectid database-id of the RPObject
	 * @return true, if it exists; false otherwise
	 * @throws SQLException in case of an database error
	 */
public boolean hasRPObject(DBTransaction transaction, int objectid) throws SQLException {
    String query = "select count(*) as amount from rpobject where object_id=[objectid]";
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("objectid", objectid);
    logger.debug("hasRPObject is executing query " + query);
    int count = transaction.querySingleCellInt(query, params);
    return count > 0;
}
