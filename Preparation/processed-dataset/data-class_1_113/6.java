/**
	 * saves an RPObject to the database
	 *
	 * @param transaction DBTransaction
	 * @param object RPObject to save
	 * @return objectid
	 * @throws IOException in case of an input/output error
	 * @throws SQLException in case of an database error
	 */
public int storeRPObject(DBTransaction transaction, RPObject object) throws IOException, SQLException {
    ByteArrayOutputStream array = new ByteArrayOutputStream();
    DeflaterOutputStream out_stream = new DeflaterOutputStream(array);
    OutputSerializer serializer = new OutputSerializer(out_stream);
    int protocolVersion = serializer.getProtocolVersion();
    try {
        object.writeObject(serializer, DetailLevel.FULL);
        out_stream.close();
    } catch (IOException e) {
        logger.warn("Error while serializing rpobject: " + object, e);
        throw e;
    }
    // setup stream for blob 
    ByteArrayInputStream inStream = new ByteArrayInputStream(array.toByteArray());
    int object_id = -1;
    if (object.has("#db_id")) {
        object_id = object.getInt("#db_id");
    }
    String query;
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("object_id", object_id);
    params.put("protocolVersion", protocolVersion);
    if (object_id != -1 && hasRPObject(transaction, object_id)) {
        query = "update rpobject set data=?, protocol_version=[protocolVersion] where object_id=[object_id]";
    } else {
        query = "insert into rpobject (data, protocol_version) values(?, [protocolVersion])";
    }
    logger.debug("storeRPObject is executing query " + query);
    transaction.execute(query, params, inStream);
    // If object is new, get the objectid we gave it. 
    if (object_id == -1) {
        object_id = transaction.getLastInsertId("rpobject", "id");
        // We alter the original object to add the proper db_id 
        object.put("#db_id", object_id);
    } else {
        object_id = object.getInt("#db_id");
    }
    return object_id;
}
