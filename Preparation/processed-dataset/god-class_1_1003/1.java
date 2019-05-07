/**
     * Serialize the specified object
     *
     * @param object - object to serialize
     * @return byte[] - the serialized object
     * @throws IOException - if there is an error with serialization
     */
public static byte[] serialize(Object object) throws IOException {
    ByteArrayOutputStream bstream = new ByteArrayOutputStream();
    ObjectOutputStream ostream = new ObjectOutputStream(bstream);
    ostream.writeObject(object);
    return bstream.toByteArray();
}
