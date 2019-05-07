/**
     * Deserialise the specified blob into an Object and return it
     * to the client.
     * <p>
     * If there is an error with the deserialization then throw an
     * IOException
     *
     * @param blob - the blob of bytes to deserialize
     * @return Object - the object to return
     * @throws IOException - if it cannot deseriliaze
     * @throws IllegalArgumentException if the blob is null
     */
public static Object deserialize(byte[] blob) throws IOException, ClassNotFoundException {
    if (blob == null) {
        throw new IllegalArgumentException("null blob to deserialize");
    }
    ByteArrayInputStream bstream = new ByteArrayInputStream(blob);
    ObjectInputStream istream = new ObjectInputStream(bstream);
    return istream.readObject();
}
