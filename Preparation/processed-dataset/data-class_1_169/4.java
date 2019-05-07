// implementation of Externalizable.writeExternal 
public void readExternal(ObjectInput stream) throws IOException, ClassNotFoundException {
    long version = stream.readLong();
    if (version == serialVersionUID) {
        _data = stream.readObject();
        super.readExternal(stream);
    } else {
        throw new IOException("No support for DataTransactionLogEntry " + "with version " + version);
    }
}
