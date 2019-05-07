/**
     * Set the data for the transaction. If the specified object is
     * not Serializable then throw an IllegalArgumentException
     *
     * @param object - object data to set
     * @throws IOException - if it cannot serialize the data
     * @throws IllegalArgumentException - if it is not serializable
     */
public void setData(Object data) throws IllegalArgumentException, IOException {
    if ((Serializable.class.isAssignableFrom(data.getClass())) || (Externalizable.class.isAssignableFrom(data.getClass()))) {
        _data = new String(SerializationHelper.serialize(data));
    } else {
        throw new IllegalArgumentException("The object to setObject must be serializable");
    }
}
