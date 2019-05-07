/**
	 * loads an RPObject form the database
	 *
	 * @param transaction DBTransaction
	 * @param objectid database-id of the RPObject
	 * @param transform use the factory to create a subclass of RPObject
	 * @return RPObject
	 * @throws IOException in case of an input/output error
	 * @throws SQLException in case of an database error
	 */
public RPObject loadRPObject(DBTransaction transaction, int objectid, boolean transform) throws SQLException, IOException {
    String query = "select data, protocol_version from rpobject where object_id=[objectid]";
    logger.debug("loadRPObject is executing query " + query);
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("objectid", objectid);
    ResultSet resultSet = transaction.query(query, params);
    if (resultSet.next()) {
        Blob data = resultSet.getBlob("data");
        int protocolVersion = NetConst.FIRST_VERSION_WITH_MULTI_VERSION_SUPPORT - 1;
        Object temp = resultSet.getObject("protocol_version");
        if (temp != null) {
            protocolVersion = ((Integer) temp).intValue();
        }
        RPObject object = readRPObject(objectid, data, protocolVersion, transform);
        resultSet.close();
        return object;
    }
    resultSet.close();
    return null;
}
