// implementation of Externalizable.writeExternal 
public void writeExternal(ObjectOutput stream) throws IOException {
    stream.writeLong(serialVersionUID);
    stream.writeObject(_data);
    super.writeExternal(stream);
}
