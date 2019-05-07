/**
	 * reads an RPObject that has already been loaded from the database
	 *
	 * @param objectid  object_id of RPObject
	 * @param data      blob data
	 * @param protocolVersion version of serialization protocol
	 * @param transform should it be transformed using the RPObjectFactory
	 * @return RPBobject
	 * @throws IOException in case of an input/output error
	 * @throws SQLException in case of an database error
	 */
public RPObject readRPObject(int objectid, Blob data, int protocolVersion, boolean transform) throws SQLException, IOException {
    InputStream input = data.getBinaryStream();
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    // set read buffer size 
    byte[] rb = new byte[1024];
    int ch = 0;
    // process blob 
    while ((ch = input.read(rb)) != -1) {
        output.write(rb, 0, ch);
    }
    byte[] content = output.toByteArray();
    input.close();
    output.close();
    ByteArrayInputStream inStream = new ByteArrayInputStream(content);
    InflaterInputStream szlib = new InflaterInputStream(inStream, new Inflater());
    InputSerializer inputSerializer = new InputSerializer(szlib);
    inputSerializer.setProtocolVersion(protocolVersion);
    RPObject object = (RPObject) inputSerializer.readObject(new RPObject());
    if (transform) {
        object = factory.transform(object);
    }
    object.put("#db_id", objectid);
    return object;
}
