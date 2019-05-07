/**
	 * deletes an RPObject from the database
	 *
	 * @param transaction DBTransaction
	 * @param objectid database-id of the RPObject
	 * @return objectid
	 * @throws SQLException in case of an database error
	 */
public int removeRPObject(DBTransaction transaction, int objectid) throws SQLException {
    String query = "delete from rpobject where object_id=[objectid]";
    logger.debug("removeRPObject is executing query " + query);
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("objectid", objectid);
    transaction.execute(query, params);
    return objectid;
}
