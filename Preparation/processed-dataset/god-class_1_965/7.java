/** Invokes {@link #setBytes(long,byte[],int,int) setBytes(pos,bytes,0,bytes.length)} */
public int setBytes(long pos, byte[] bytes) throws SQLException {
    return setBytes(pos, bytes, 0, bytes.length);
}
